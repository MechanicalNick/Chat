package com.tinkoff.homework.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.ActivityMainBinding
import com.tinkoff.homework.date.DateDelegate
import com.tinkoff.homework.itemdecorator.MarginItemDecorator
import com.tinkoff.homework.message.MessageDelegate
import com.tinkoff.homework.message.MessageModel
import com.tinkoff.homework.utils.MessageAdapter
import com.tinkoff.homework.utils.MessageFactory
import java.time.LocalDate


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var messageFactory: MessageFactory
    private val adapter: MessageAdapter by lazy { MessageAdapter() }
    private val space = 32

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        createRecyclerView()
        setContentView(binding.root)
    }

    private fun createRecyclerView() {
        messageFactory = MessageFactory(adapter, stubMessages)
        adapter.apply {
            addDelegate(MessageDelegate())
            addDelegate(DateDelegate())
        }
        var itemDecoration = MarginItemDecorator(
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

        private val stubMessages = listOf(
            MessageModel(
                id = 1,
                text = "HELLO WORLD",
                date = SEP_1
            ),
            MessageModel(
                id = 2,
                text = "Connected to the target VM, address: 'localhost:54897', transport: 'socket'",
                date = SEP_1
            ),
            MessageModel(
                id = 3,
                text = "Capturing and displaying logcat messages from application. This behavior can be disabled in the \"Logcat output\" section of the \"Debugger\" settings page.",
                date = SEP_1
            ),
            MessageModel(
                id = 4,
                text = "W/inkoff.homewor: Unexpected CPU variant for X86 using defaults: x86",
                date = SEP_12
            ),
        )
    }
}