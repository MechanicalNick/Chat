package com.tinkoff.homework.use_cases

import com.tinkoff.homework.data.MessageModel
import com.tinkoff.homework.data.Reaction
import com.tinkoff.homework.utils.Const
import java.time.LocalDate

object FakeMessage {
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

    fun getFakeData(): List<MessageModel>{
        return stubMessages
    }
}
