package com.tinkoff.homework.data

import java.time.LocalDate
import java.util.*

class MessageModel(
    val id: Long, val senderId: Long, val senderFullName: String, val text: String, val date: LocalDate,
    val avatarUrl: String, val reactions: MutableList<Reaction>
)