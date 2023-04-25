package com.tinkoff.homework.elm.chat.model

import android.net.Uri
import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.data.domain.Reaction
import com.tinkoff.homework.data.dto.ImageResponse

sealed class ChatEvent {

    sealed class Ui : ChatEvent() {
        object Init : Ui()
        data class LoadData(val topicName: String, val streamId: Long) : Ui()
        data class LoadImage(val uri: Uri, val topicName: String, val streamId: Long) : Ui()
        object LoadNextPage : Ui()
        data class LoadCashedData(val topicName: String, val streamId: Long) : Ui()
        data class AddReaction(val messageId: Long, val reaction: Reaction) : Ui()
        data class RemoveReaction(val messageId: Long, val reaction: Reaction) : Ui()
        data class SendMessage(val streamId: Long, val topic: String, val message: String) : Ui()
    }

    sealed class Internal : ChatEvent() {
        data class PageDataLoaded(val messages: List<MessageModel>) : Internal()
        data class DataLoaded(val messages: List<MessageModel>) : Internal()
        data class ErrorLoading(val error: Throwable) : Internal()
        data class ReactionAdded(val messageId: Long, val reaction: Reaction) : Internal()
        data class ReactionRemoved(val messageId: Long, val reaction: Reaction) : Internal()
        data class MessageSent(
            val messageId: Long,
            val streamId: Long,
            val topic: String,
            val message: String
        ) : Internal()

        data class ImageLoaded(
            val response: ImageResponse,
            val streamId: Long,
            val topicName: String
        ) : Internal()
    }
}