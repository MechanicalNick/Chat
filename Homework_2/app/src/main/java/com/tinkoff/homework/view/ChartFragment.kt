package com.tinkoff.homework.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tinkoff.homework.data.Reaction
import com.tinkoff.homework.databinding.ChartFragmentBinding
import com.tinkoff.homework.date.DateDelegate
import com.tinkoff.homework.itemdecorator.MarginItemDecorator
import com.tinkoff.homework.message.MessageDelegate
import com.tinkoff.homework.message.MessageModel
import com.tinkoff.homework.utils.Const
import com.tinkoff.homework.utils.MessageAdapter
import com.tinkoff.homework.utils.MessageFactory
import com.tinkoff.homework.viewmodel.MainViewModel
import java.time.LocalDate

class ChartFragment : Fragment() {
    private lateinit var messageFactory: MessageFactory
    private lateinit var bottomFragment: BottomFragment

    private var _binding: ChartFragmentBinding? = null

    private val mainViewModel: MainViewModel by viewModels()
    private val adapter: MessageAdapter by lazy { MessageAdapter() }
    private val space = 32
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bottomFragment = BottomFragment()
        _binding = ChartFragmentBinding.inflate(inflater, container, false)
        createRecyclerView()
        mainViewModel.addEmoji.observe(viewLifecycleOwner) {
            messageFactory.addEmoji(it)
        }
        mainViewModel.removeEmoji.observe(viewLifecycleOwner) {
            messageFactory.removeEmoji(it)
        }
        return view
    }

    private fun createRecyclerView() {
        messageFactory = MessageFactory(adapter, stubMessages)
        adapter.apply {
            addDelegate(MessageDelegate(bottomFragment, parentFragmentManager, mainViewModel))
            addDelegate(DateDelegate())
        }
        val itemDecoration = MarginItemDecorator(
            space,
            binding.root.orientation
        )
        binding.recycler.addItemDecoration(itemDecoration)
        binding.recycler.adapter = adapter

        binding.contentEditor.arrowButton.setOnClickListener {
            messageFactory.addText(binding.contentEditor.editText.text)
            binding.contentEditor.editText.text.clear()
        }
    }

    companion object {

        private val SEP_1 = LocalDate.of(2023, 9, 1)
        private val SEP_12 = LocalDate.of(2023, 9, 12)

        private val firstReaction = Reaction(0x1f539, mutableListOf(Const.myId, 2))
        private val secondReaction = Reaction(0x1f198, mutableListOf(2))

        private val stubMessages = listOf(
            MessageModel(
                id = 1,
                text = "HELLO WORLD",
                date = SEP_1,
                mutableListOf(firstReaction, secondReaction)
            ),
            MessageModel(
                id = 2,
                text = "Connected to the target VM, address: 'localhost:54897', transport: 'socket'",
                date = SEP_1,
                mutableListOf()
            ),
            MessageModel(
                id = 3,
                text = "Capturing and displaying logcat messages from application. This behavior can be disabled in the \"Logcat output\" section of the \"Debugger\" settings page.",
                date = SEP_1,
                mutableListOf()
            ),
            MessageModel(
                id = 4,
                text = "W/inkoff.homewor: Unexpected CPU variant for X86 using defaults: x86",
                date = SEP_12,
                mutableListOf()
            ),
        )
    }
}