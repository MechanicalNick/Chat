package com.tinkoff.homework.message

import com.tinkoff.homework.data.Reaction
import java.time.LocalDate

data class MessageModel(
    val id: Int, val text: String, val date: LocalDate,
    val reactions: MutableList<Reaction>
)