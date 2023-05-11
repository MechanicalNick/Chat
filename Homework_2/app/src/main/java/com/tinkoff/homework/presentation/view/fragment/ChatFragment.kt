package com.tinkoff.homework.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.model.LazyHeaders
import com.github.terrakok.cicerone.Router
import com.tinkoff.homework.R
import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.databinding.FragmentChatBinding
import com.tinkoff.homework.di.component.DaggerChatComponent
import com.tinkoff.homework.domain.data.Reaction
import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.ViewState
import com.tinkoff.homework.elm.chat.model.ChatEffect
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.elm.chat.model.ChatState
import com.tinkoff.homework.getAppComponent
import com.tinkoff.homework.navigation.MessageFactory
import com.tinkoff.homework.presentation.DelegatesAdapter
import com.tinkoff.homework.presentation.view.adapter.date.DateDelegate
import com.tinkoff.homework.presentation.view.adapter.message.CompanionMessageDelegate
import com.tinkoff.homework.presentation.view.adapter.message.MyMessageDelegate
import com.tinkoff.homework.presentation.view.itemdecorator.MarginItemDecorator
import com.tinkoff.homework.presentation.viewmodel.ChatViewModel
import javax.inject.Inject

class ChatFragment : BaseFragment<ChatEvent, ChatEffect, ChatState>(), ChatFragmentCallback {
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

    override val initEvent = ChatEvent.Ui.Init

    private lateinit var bottomFragment: BottomFragment

    private var _binding: FragmentChatBinding? = null

    private val chatViewModel: ChatViewModel by viewModels()
    private val adapter: DelegatesAdapter by lazy { DelegatesAdapter() }
    private val space = 32
    private val binding get() = _binding!!
    private var streamId: Long? = null
    private var topicName: String = ""
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
        bottomFragment = BottomFragment()
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        topicName = requireArguments().getString(ARG_TOPIC)!!
        binding.header.text = getString(R.string.sharp, topicName)

        val streamName = requireArguments().getString(ARG_STREAM)
        binding.chatToolbar.title = getString(R.string.sharp, streamName)

        streamId = requireArguments().getLong(ARG_STREAM_ID)

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
                    adapter.submitList(messageFactory.init(state.items, credentials.id))
                }
            }
        }
        binding.progressBar.isVisible = state.isShowProgress
    }

    override fun handleEffect(effect: ChatEffect) {
        return when(effect){
            ChatEffect.ScrollToLastElement ->
                binding.recycler.scrollToPosition(messageFactory.getCount() - 1)
            ChatEffect.SmoothScrollToLastElement ->
                binding.recycler.smoothScrollToPosition(messageFactory.getCount() - 1)
        }
    }

    private fun loadData() {
        this.store.accept(ChatEvent.Ui.LoadCashedData(topicName, streamId!!))
        this.store.accept(ChatEvent.Ui.LoadData(topicName, streamId!!))
    }

    private fun createRecyclerView() {
        adapter.apply {
            addDelegate(MyMessageDelegate(this@ChatFragment, lazyHeaders, isUserImageRegex))
            addDelegate(CompanionMessageDelegate(this@ChatFragment, lazyHeaders, isUserImageRegex))
            addDelegate(DateDelegate())
        }
        val itemDecoration = MarginItemDecorator(
            space,
            binding.chatData.orientation
        )
        binding.recycler.addItemDecoration(itemDecoration)
        binding.recycler.addOnScrollListener(
            ChatScrollListener(
                binding.recycler.layoutManager as LinearLayoutManager,
                chatViewModel.store
            )
        )
        binding.recycler.adapter = adapter
    }

    private fun subscribeToSendMessage(){
        binding.contentEditor.arrowButton.setOnClickListener {
            val message = binding.contentEditor.editText.text.toString()
            binding.contentEditor.editText.text.clear()
            streamId?.let { this.store.accept(ChatEvent.Ui.SendMessage(it, topicName, message)) }
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


    override fun reactionChange(reaction: Reaction, messageId: Long, senderId: Long) {
        this.store.accept(ChatEvent.Ui.ChangeReaction(messageId, reaction))
    }

    override fun showBottomSheetDialog(id: Long, senderId: Long): Boolean {
        bottomFragment.show(childFragmentManager, null)
        val args = Bundle()
        args.putLong(ARG_MODEL_ID, id)
        args.putLong(ARG_SENDER_ID, senderId)
        bottomFragment.arguments = args
        return true
    }

    companion object {
        const val ARG_MODEL_ID = "modelId"
        const val ARG_SENDER_ID = "senderId"

        const val ARG_TOPIC = "topicName"
        const val ARG_STREAM = "steamName"
        const val ARG_STREAM_ID = "steamId"

        fun newInstance(topicName: String, streamName: String, streamId: Long): ChatFragment {
            val fragment = ChatFragment()
            val arguments = Bundle()
            arguments.putString(ARG_TOPIC, topicName)
            arguments.putString(ARG_STREAM, streamName)
            arguments.putLong(ARG_STREAM_ID, streamId)
            fragment.arguments = arguments
            return fragment
        }
    }
}