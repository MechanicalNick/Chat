package com.tinkoff.homework.elm.chat.model

import android.net.Uri
import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.data.Reaction

sealed class ChatCommand {
    data class ChangeTopic(val messageId: Long, val topicName: String) : ChatCommand()
    data class LoadCashedData(val topicName: String, val streamId: Long) : ChatCommand()
    data class LoadData(val topicName: String, val streamId: Long) : ChatCommand()
    data class LoadImage(val uri: Uri, val topicName: String, val streamId: Long) : ChatCommand()
    data class LoadNextPage(val messageId: Long, val topicName: String, val streamId: Long) :
        ChatCommand()

    data class AddReaction(val messageId: Long, val reaction: Reaction) : ChatCommand()
    data class RemoveReaction(val messageId: Long, val reaction: Reaction) : ChatCommand()
    data class ChangeReaction(val message: MessageModel, val reaction: Reaction) : ChatCommand()
    data class SendMessage(val streamId: Long, val topic: String, val message: String) :
        ChatCommand()

    data class RemoveMessage(val messageId: Long) : ChatCommand()
    data class EditMessage(val messageId: Long, val newText: String) : ChatCommand()
}