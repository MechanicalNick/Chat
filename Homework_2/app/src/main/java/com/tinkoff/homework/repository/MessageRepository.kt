package com.tinkoff.homework.repository

import com.tinkoff.homework.data.MessageModel
import com.tinkoff.homework.data.dto.MessageResponse
import io.reactivex.Single

interface MessageRepository {
    fun getMessages(
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long
    ): Single<List<MessageModel>>

    fun addReaction(messageId: Long, emojiName: String): Single<MessageResponse>
    fun removeReaction(messageId: Long, emojiName: String): Single<MessageResponse>
    fun sendMessage(streamId: Long, topic: String, message: String): Single<MessageResponse>
}