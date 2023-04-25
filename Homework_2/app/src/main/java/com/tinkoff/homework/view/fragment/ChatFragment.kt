package com.tinkoff.homework.view.fragment

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
import com.github.terrakok.cicerone.Router
import com.tinkoff.homework.R
import com.tinkoff.homework.data.domain.Reaction
import com.tinkoff.homework.databinding.ChartFragmentBinding
import com.tinkoff.homework.di.component.DaggerChatComponent
import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.chat.model.ChatEffect
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.elm.chat.model.ChatState
import com.tinkoff.homework.getAppComponent
import com.tinkoff.homework.utils.Const
import com.tinkoff.homework.utils.MessageFactory
import com.tinkoff.homework.utils.adapter.DelegatesAdapter
import com.tinkoff.homework.utils.adapter.date.DateDelegate
import com.tinkoff.homework.utils.adapter.message.CompanionMessageDelegate
import com.tinkoff.homework.utils.adapter.message.MyMessageDelegate
import com.tinkoff.homework.view.itemdecorator.MarginItemDecorator
import com.tinkoff.homework.viewmodel.ChatViewModel
import javax.inject.Inject

class ChatFragment : BaseFragment<ChatEvent, ChatEffect, ChatState>(), ChatFragmentCallback {

    @Inject
    lateinit var router: Router

    @Inject
    override lateinit var factory: BaseStoreFactory<ChatEvent, ChatEffect, ChatState>

    @Inject
    lateinit var messageFactory: MessageFactory

    override val initEvent = ChatEvent.Ui.Init

    private lateinit var bottomFragment: BottomFragment

    private var _binding: ChartFragmentBinding? = null

    private val chatViewModel: ChatViewModel by viewModels()
    private val adapter: DelegatesAdapter by lazy { DelegatesAdapter() }
    private val space = 32
    private val binding get() = _binding!!
    private var streamId: Long? = null
    private var topicName: String = ""


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
        _binding = ChartFragmentBinding.inflate(inflater, container, false)

        topicName = requireArguments().getString(ARG_TOPIC)!!
        binding.header.text = getString(R.string.sharp, topicName)

        val streamName = requireArguments().getString(ARG_STREAM)
        binding.toolbar.title = streamName

        streamId = requireArguments().getLong(ARG_STREAM_ID)

        binding.toolbar.setNavigationOnClickListener {
            router.exit()
        }

        chatViewModel.store = this.store

        createRecyclerView()
        loadData()

        return binding.root
    }

    override fun render(state: ChatState) {
        binding.progressBar.isVisible = state.isShowProgress

        if(state.error == null) {
            binding.errorStateContainer.errorLayout.isVisible = false
            binding.recycler.isVisible = true
            binding.shimmer.isVisible = true
            if (state.items.isNullOrEmpty()) {
                binding.shimmer.showShimmer(true)
            } else {
                binding.shimmer.hideShimmer()
                adapter.submitList(messageFactory.init(state.items, Const.myId))
            }
        } else{
            binding.errorStateContainer.errorLayout.isVisible = true
            binding.shimmer.isVisible = false
            binding.recycler.isVisible = false
            binding.errorStateContainer.errorText.text = state.error.message
            binding.errorStateContainer.retryButton.setOnClickListener(){
                loadData()
            }
        }
    }

    override fun handleEffect(effect: ChatEffect): Unit? {
        return when(effect){
            ChatEffect.ScrollToLastElement ->
                binding.recycler.scrollToPosition(messageFactory.getCount() - 1)
        }
    }

    private fun loadData() {
        this.store.accept(ChatEvent.Ui.LoadCashedData(topicName, streamId!!))
        this.store.accept(ChatEvent.Ui.LoadData(topicName, streamId!!))
    }

    private fun createRecyclerView() {
        adapter.apply {
            addDelegate(MyMessageDelegate(this@ChatFragment))
            addDelegate(CompanionMessageDelegate(this@ChatFragment))
            addDelegate(DateDelegate())
        }
        val itemDecoration = MarginItemDecorator(
            space,
            binding.linearLayout.orientation
        )
        binding.recycler.addItemDecoration(itemDecoration)
        binding.recycler.addOnScrollListener(
            ChatScrollListener(
                binding.recycler.layoutManager as LinearLayoutManager,
                chatViewModel.store
            )
        )
        binding.recycler.adapter = adapter

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

        binding.contentEditor.arrowButton.setOnClickListener {
            val message = binding.contentEditor.editText.text.toString()
            binding.contentEditor.editText.text.clear()
            streamId?.let { this.store.accept(ChatEvent.Ui.SendMessage(it, topicName, message)) }
            binding.recycler.scrollToPosition(messageFactory.getCount() - 1)
        }
    }

    override fun reactionRemove(reaction: Reaction, messageId: Long, senderId: Long) {
        this.store.accept(ChatEvent.Ui.RemoveReaction(messageId, reaction))
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

        private const val ARG_TOPIC = "topicName"
        private const val ARG_STREAM = "steamName"
        private const val ARG_STREAM_ID = "steamId"

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