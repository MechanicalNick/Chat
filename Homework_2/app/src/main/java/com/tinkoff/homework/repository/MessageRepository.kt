package com.tinkoff.homework.repository

import com.tinkoff.homework.data.MessageModel
import com.tinkoff.homework.data.dto.ReactionsResponse
import io.reactivex.Single

interface MessageRepository {
    fun getMessages(
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long
    ): Single<List<MessageModel>>

    fun addReaction(messageId: Long, emojiName: String): Single<ReactionsResponse>

    fun removeReaction(messageId: Long, emojiName: String): Single<ReactionsResponse>
}