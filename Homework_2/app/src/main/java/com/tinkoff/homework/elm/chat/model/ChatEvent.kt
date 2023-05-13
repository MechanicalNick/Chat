package com.tinkoff.homework.elm.chat.model

import android.net.Uri
import com.tinkoff.homework.data.dto.ImageResponse
import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.data.Reaction

sealed class ChatEvent {

    sealed class Ui : ChatEvent() {
        object Init : Ui()
        data class ChangeTopic(val messageId: Long, val newTopic: String) : Ui()
        data class EditMessage(val messageId: Long, val newText: String) : Ui()
        data class RemoveMessage(val messageId: Long) : Ui()
        data class ShowSnackbar(val message: String) : Ui()
        data class LoadData(val topicName: String, val streamId: Long) : Ui()
        data class LoadImage(val uri: Uri, val topicName: String, val streamId: Long) : Ui()
        data class LoadNextPage(val topicName: String) : Ui()
        data class LoadCashedData(val topicName: String, val streamId: Long) : Ui()
        data class AddReaction(val messageId: Long, val reaction: Reaction) : Ui()
        data class RemoveReaction(val messageId: Long, val reaction: Reaction) : Ui()
        data class ChangeReaction(val message: MessageModel, val reaction: Reaction) : Ui()
        data class SendMessage(val streamId: Long, val topic: String, val message: String) : Ui()
        data class GoToChat(
            val topicName: String,
            val streamName: String,
                            val streamId: Long) : Ui()
    }

    sealed class Internal : ChatEvent() {
        data class PageDataLoaded(val messages: List<MessageModel>) : Internal()
        data class DataLoaded(val messages: List<MessageModel>) : Internal()
        data class ErrorLoading(val error: Throwable) : Internal()
        data class TimeLimitError(val error: Throwable) : Internal()
        data class ReactionAdded(val messageId: Long, val reaction: Reaction) : Internal()
        data class ReactionRemoved(val messageId: Long, val reaction: Reaction) : Internal()
        data class MessageRemoved(val messageId: Long) : Ui()
        data class MessageEdited(val messageId: Long, val newText: String) : Ui()
        data class TopicChanged(val messageId: Long, val newTopic: String) : Ui()
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