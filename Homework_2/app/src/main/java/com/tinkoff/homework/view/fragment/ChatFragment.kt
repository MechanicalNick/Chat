package com.tinkoff.homework.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.homework.App
import com.tinkoff.homework.R
import com.tinkoff.homework.data.MessageModel
import com.tinkoff.homework.data.Reaction
import com.tinkoff.homework.databinding.ChartFragmentBinding
import com.tinkoff.homework.navigation.NavigationScreens
import com.tinkoff.homework.utils.Const
import com.tinkoff.homework.utils.MessageFactory
import com.tinkoff.homework.utils.UiState
import com.tinkoff.homework.utils.adapter.DelegatesAdapter
import com.tinkoff.homework.utils.adapter.date.DateDelegate
import com.tinkoff.homework.utils.adapter.message.CompanionMessageDelegate
import com.tinkoff.homework.utils.adapter.message.MyMessageDelegate
import com.tinkoff.homework.view.itemdecorator.MarginItemDecorator
import com.tinkoff.homework.viewmodel.ChatViewModel
import javax.inject.Inject

class ChatFragment: Fragment(), ChatFragmentCallback {

    @Inject
    lateinit var router: Router
    private lateinit var messageFactory: MessageFactory
    private lateinit var bottomFragment: BottomFragment

    private var _binding: ChartFragmentBinding? = null

    private val chatViewModel: ChatViewModel by viewModels()
    private val adapter: DelegatesAdapter by lazy { DelegatesAdapter() }
    private val space = 32
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        App.INSTANCE.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bottomFragment = BottomFragment()
        _binding = ChartFragmentBinding.inflate(inflater, container, false)

        chatViewModel.addEmoji.observe(viewLifecycleOwner) {
            messageFactory.addEmoji(it)
        }
        chatViewModel.removeEmoji.observe(viewLifecycleOwner) {
            messageFactory.removeEmoji(it)
        }

        chatViewModel.state.observe(viewLifecycleOwner){
            render(it)
        }

        val topicName = requireArguments().getString(ARG_TOPIC)
        binding.header.text = getString(R.string.sharp, topicName)

        val streamName = requireArguments().getString(ARG_STREAM)
        binding.toolbar.title = streamName

        val streamId = requireArguments().getLong(ARG_STREAM_ID)

        chatViewModel.init(topicName!!, streamId)

        binding.toolbar.setNavigationOnClickListener {
            router.backTo(NavigationScreens.channels())
        }

        return binding.root
    }

    private fun render(state: UiState<List<MessageModel>>) {
        when (state) {
            is UiState.Loading<List<MessageModel>> -> {
                binding.shimmer.showShimmer(true)
            }
            is UiState.Data<List<MessageModel>> -> {
                createRecyclerView(state.data)
                binding.shimmer.hideShimmer()
            }
            is UiState.Error<List<MessageModel>> -> {
                binding.shimmer.hideShimmer()
                Snackbar.make(
                    binding.root, state.exception.toString(),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun createRecyclerView(data: List<MessageModel>) {
        messageFactory = MessageFactory(adapter, data, Const.myId)
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
        binding.recycler.adapter = adapter

        binding.contentEditor.arrowButton.setOnClickListener {
            messageFactory.addText(binding.contentEditor.editText.text)
            binding.contentEditor.editText.text.clear()
            binding.recycler.scrollToPosition(messageFactory.getCount() - 1);
        }
    }

    override fun reactionRemove(reaction: Reaction, messageId: Long) {
        chatViewModel.removeEmoji(messageId, reaction.emojiCode, reaction.emojiName)
    }

    override fun showBottomSheetDialog(id: Long): Boolean {
        bottomFragment.show(childFragmentManager, null)
        val args = Bundle()
        args.putLong("modelId", id)
        bottomFragment.arguments = args
        return true
    }

    companion object {
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