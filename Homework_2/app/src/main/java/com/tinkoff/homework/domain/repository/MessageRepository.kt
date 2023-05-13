package com.tinkoff.homework.domain.repository

import android.net.Uri
import com.tinkoff.homework.data.dto.ImageResponse
import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.domain.data.MessageModel
import io.reactivex.Single

interface MessageRepository {
    fun fetchMessages(
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long?,
        query: String
    ): Single<List<MessageModel>>
    fun loadResultsFromServer(
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long?,
        query: String
    ): Single<List<MessageModel>>

    fun fetchCashedMessages(streamId: Long, topic: String): Single<List<MessageModel>>
    fun addReaction(messageId: Long, emojiName: String): Single<MessageResponse>
    fun removeReaction(messageId: Long, emojiName: String): Single<MessageResponse>
    fun sendMessage(streamId: Long, topic: String, message: String): Single<MessageResponse>
    fun sendImage(uri: Uri): Single<ImageResponse>
    fun removeMessage(messageId: Long): Single<MessageResponse>
    fun editMessage(messageId: Long, newText: String): Single<MessageResponse>
    fun changeTopic(messageId: Long, newTopic: String): Single<MessageResponse>
}