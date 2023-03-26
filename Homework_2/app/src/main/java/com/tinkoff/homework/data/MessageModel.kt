package com.tinkoff.homework.data

import java.time.LocalDate

data class MessageModel(
    val id: Int, val text: String, val date: LocalDate,
    val reactions: MutableList<Reaction>
)