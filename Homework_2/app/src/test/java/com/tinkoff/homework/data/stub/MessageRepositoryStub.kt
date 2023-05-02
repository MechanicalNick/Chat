package com.tinkoff.homework.data.stub

import android.net.Uri
import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.data.dto.ImageResponse
import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.repository.interfaces.MessageRepository
import io.reactivex.Single

class MessageRepositoryStub : MessageRepository {
    var messageProvider: () -> Single<List<MessageModel>> = { Single.just(emptyList()) }

    override fun fetchMessages(
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long?,
        query: String
    ): Single<List<MessageModel>> = messageProvider()
        .map { it.filter { messageModel -> messageModel.streamId == streamId
                && messageModel.subject == topic }
        }

    override fun fetchCashedMessages(streamId: Long, topic: String): Single<List<MessageModel>> {
        TODO("Not yet implemented")
    }

    override fun addReaction(messageId: Long, emojiName: String): Single<MessageResponse> {
        TODO("Not yet implemented")
    }

    override fun removeReaction(messageId: Long, emojiName: String): Single<MessageResponse> {
        TODO("Not yet implemented")
    }

    override fun sendMessage(
        streamId: Long,
        topic: String,
        message: String
    ): Single<MessageResponse> {
        TODO("Not yet implemented")
    }

    override fun sendImage(uri: Uri): Single<ImageResponse> {
        TODO("Not yet implemented")
    }
}