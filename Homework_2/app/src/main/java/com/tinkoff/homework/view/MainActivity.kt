package com.tinkoff.homework.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.ActivityMainBinding
import com.tinkoff.homework.date.DateDelegate
import com.tinkoff.homework.date.DateModel
import com.tinkoff.homework.itemdecorator.MarginItemDecorator
import com.tinkoff.homework.message.MessageDelegate
import com.tinkoff.homework.message.MessageModel
import com.tinkoff.homework.utils.MessageAdapter
import com.tinkoff.homework.utils.concatenateWithDate


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter: MessageAdapter by lazy { MessageAdapter() }
    private val space = 32

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        adapter.apply {
            addDelegate(MessageDelegate())
            addDelegate(DateDelegate())
        }
        var dividerItemDecoration = MarginItemDecorator(
            space,
            binding.root.orientation
        )
        binding.recycler.addItemDecoration(dividerItemDecoration)
        binding.recycler.adapter = adapter
        adapter.submitList(stubMessages.concatenateWithDate(stubDatesList))
        setContentView(binding.root)
    }
    companion object {

        private const val SEP_1 = "1 сенятбря"
        private const val SEP_12 = "12 сенятбря"

        private val stubDatesList = listOf(
            DateModel(
                id = 1,
                date = SEP_1,
            ),
            DateModel(
                id = 2,
                date = SEP_12,
            ),
        )

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