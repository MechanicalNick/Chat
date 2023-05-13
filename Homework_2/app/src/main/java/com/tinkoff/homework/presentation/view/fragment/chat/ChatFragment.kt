package com.tinkoff.homework.presentation.view.fragment.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.model.LazyHeaders
import com.github.terrakok.cicerone.Router
import com.tinkoff.homework.R
import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.databinding.FragmentChatBinding
import com.tinkoff.homework.di.component.DaggerChatComponent
import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.data.Reaction
import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.ViewState
import com.tinkoff.homework.elm.chat.model.ChatEffect
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.elm.chat.model.ChatState
import com.tinkoff.homework.getAppComponent
import com.tinkoff.homework.navigation.NavigationScreens
import com.tinkoff.homework.presentation.view.ChatFragmentCallback
import com.tinkoff.homework.presentation.view.ChatScrollListener
import com.tinkoff.homework.presentation.view.MessageFactory
import com.tinkoff.homework.presentation.view.ToChatRouter
import com.tinkoff.homework.presentation.view.adapter.DelegatesAdapter
import com.tinkoff.homework.presentation.view.adapter.date.DateDelegate
import com.tinkoff.homework.presentation.view.adapter.message.CompanionMessageDelegate
import com.tinkoff.homework.presentation.view.adapter.message.MyMessageDelegate
import com.tinkoff.homework.presentation.view.adapter.message.TopicMessageDelegate
import com.tinkoff.homework.presentation.view.fragment.ActionSelectorFragment
import com.tinkoff.homework.presentation.view.fragment.BaseFragment
import com.tinkoff.homework.presentation.view.itemdecorator.MarginItemDecorator
import com.tinkoff.homework.presentation.viewmodel.ChatViewModel
import javax.inject.Inject

abstract class ChatFragment : BaseFragment<ChatEvent, ChatEffect, ChatState>(),
    ChatFragmentCallback, ToChatRouter  {
    @Inject
    lateinit var credentials: Credentials

    @Inject
    lateinit var router: Router

    @Inject
    override lateinit var factory: BaseStoreFactory<ChatEvent, ChatEffect, ChatState>

    @Inject
    lateinit var messageFactory: MessageFactory

    @Inject
    lateinit var lazyHeaders: LazyHeaders

    @Inject
    lateinit var chatViewModel: ChatViewModel

    override val initEvent = ChatEvent.Ui.Init

    private val actionSelectorFragment by lazy { ActionSelectorFragment() }

    private var _binding: FragmentChatBinding? = null
    private val adapter: DelegatesAdapter by lazy { DelegatesAdapter() }
    private val space = 32
    protected val binding get() = _binding!!
    protected var streamId: Long? = null
    protected var topicName: String = ""
    private var streamName: String? = null
    // [my dog](/user_uploads/54137/TFFOPnsTF2C9Z1t2MfBwLh66/image.jpg)
    // Паттерн: []()
    private val isUserImageRegex = Regex("\\[(.*?)\\](\\((.*?)\\))")

    override fun onAttach(context: Context) {
        DaggerChatComponent.factory()
            .create(context.getAppComponent())
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        streamName = requireArguments().getString(ARG_STREAM)!!
        topicName = requireArguments().getString(ARG_TOPIC)!!
        streamId = requireArguments().getLong(ARG_STREAM_ID)

        renderAdditionalViews()

        binding.chatToolbar.title = getString(R.string.sharp, streamName)

        binding.chatToolbar.setNavigationOnClickListener {
            router.exit()
        }

        chatViewModel.store = this.store

        createRecyclerView()
        subscribeToSendMessage()
        createMediaPicker()

        loadData()

        binding.errorStateContainer.retryButton.setOnClickListener {
            loadData()
        }

        return binding.root
    }

    override fun render(state: ChatState) {
        when(state.state){
            ViewState.Loading -> {
                renderLoadingState(
                    binding.shimmer.root,
                    binding.errorStateContainer.root,
                    binding.chatData
                )
            }
            ViewState.Error -> {
                renderErrorState(
                    binding.shimmer.root,
                    binding.errorStateContainer.root,
                    binding.chatData
                )
                state.error?.let { throwable ->
                    binding.errorStateContainer.errorText.text = throwable.message
                }
            }
            ViewState.ShowData -> {
                renderDataState(
                    binding.shimmer.root,
                    binding.errorStateContainer.root,
                    binding.chatData
                )
                state.items?.let {
                    adapter.submitList(messageFactory.init(
                        messages = state.items,
                        myId = credentials.id,
                        streamId = streamId!!,
                        streamName = streamName!!,
                        needGroupByTopic = needGroupByTopic
                    ))
                }
            }
        }
        binding.progressBar.isVisible = state.isShowProgress
    }

    override fun handleEffect(effect: ChatEffect) {
        return when (effect) {
            is ChatEffect.ScrollToLastElement ->
                binding.recycler.scrollToPosition(messageFactory.getCount() - 1)

            is ChatEffect.SmoothScrollToLastElement ->
                binding.recycler.smoothScrollToPosition(messageFactory.getCount() - 1)

            is ChatEffect.GoToChat -> router.navigateTo(
                NavigationScreens.chat(
                    effect.topicName,
                    effect.streamName, effect.streamId
                )
            )

            is ChatEffect.ShowToast ->
                context?.let {
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                } ?: Unit

            is ChatEffect.ShowTimeLimitToast ->
                context?.let {
                    Toast.makeText(
                        it,
                        it.getString(R.string.time_limit_message_error),
                        Toast.LENGTH_LONG
                    ).show()
                } ?: Unit
        }
    }

    private fun loadData() {
        streamId?.let {
            this.store.accept(ChatEvent.Ui.LoadCashedData(topicName, it))
            this.store.accept(ChatEvent.Ui.LoadData(topicName, it))
        }
    }

    private fun createRecyclerView() {
        adapter.apply {
            addDelegate(MyMessageDelegate(this@ChatFragment, lazyHeaders, isUserImageRegex))
            addDelegate(CompanionMessageDelegate(this@ChatFragment, lazyHeaders, isUserImageRegex))
            addDelegate(DateDelegate())
            addDelegate(TopicMessageDelegate(this@ChatFragment))
        }
        val itemDecoration = MarginItemDecorator(
            space,
            binding.chatData.orientation
        )
        binding.recycler.addItemDecoration(itemDecoration)
        binding.recycler.addOnScrollListener(
            ChatScrollListener(
                binding.recycler.layoutManager as LinearLayoutManager,
                chatViewModel.store,
                topicName
            )
        )
        binding.recycler.adapter = adapter
    }


    abstract val needGroupByTopic: Boolean
    abstract fun sendMessage(message: String)
    abstract fun renderAdditionalViews()

    private fun subscribeToSendMessage(){
        binding.contentEditor.arrowButton.setOnClickListener {
            val message = binding.contentEditor.editText.text.toString()
            binding.contentEditor.editText.text.clear()
            sendMessage(message)
            binding.recycler.scrollToPosition(messageFactory.getCount() - 1)
        }
    }

    private fun createMediaPicker(){
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                this.store.accept(ChatEvent.Ui.LoadImage(uri, topicName, streamId!!))
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        binding.contentEditor.plusButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    override fun reactionChange(reaction: Reaction, message: MessageModel, senderId: Long) {
        this.store.accept(ChatEvent.Ui.ChangeReaction(message, reaction))
    }

    override fun showBottomSheetDialog(id: Long, senderId: Long): Boolean {
        val args = Bundle()
        args.putLong(ARG_MODEL_ID, id)
        args.putLong(ARG_SENDER_ID, senderId)
        actionSelectorFragment.arguments = args
        actionSelectorFragment.show(childFragmentManager, null)
        return true
    }

    override fun goToChat(topicName: String, streamName: String, streamId: Long) {
        store.accept(ChatEvent.Ui.GoToChat(topicName, streamName, streamId))
    }

    companion object {
        const val ARG_MODEL_ID = "modelId"
        const val ARG_SENDER_ID = "senderId"

        const val ARG_TOPIC = "topicName"
        const val ARG_STREAM = "steamName"
        const val ARG_STREAM_ID = "steamId"

        fun newInstance(topicName: String, streamName: String, streamId: Long): ChatFragment {
            val fragment =
                if(topicName.isNotBlank()) SingleTopicChatFragment() else AllTopicsChatFragment()
            val arguments = Bundle()
            arguments.putString(ARG_TOPIC, topicName)
            arguments.putString(ARG_STREAM, streamName)
            arguments.putLong(ARG_STREAM_ID, streamId)
            fragment.arguments = arguments
            return fragment
        }
    }
}