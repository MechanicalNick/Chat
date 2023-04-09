package com.tinkoff.homework.data.domain

import java.time.LocalDate

class MessageModel(
    val id: Long, val senderId: Long, val senderFullName: String, val text: String, val date: LocalDate,
    val avatarUrl: String, val reactions: MutableList<Reaction>
)