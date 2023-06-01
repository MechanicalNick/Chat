package com.tinkoff.homework.domain.data

import java.time.LocalDateTime

class MessageModel(
    val id: Long,
    val senderId: Long,
    val senderFullName: String,
    val topic: String,
    val streamId: Long,
    val text: String,
    val dateTime: LocalDateTime,
    val avatarUrl: String,
    val reactions: MutableList<Reaction>,
    val imageUrl: String?
)