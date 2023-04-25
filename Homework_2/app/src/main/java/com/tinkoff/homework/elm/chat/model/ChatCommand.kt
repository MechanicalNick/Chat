package com.tinkoff.homework.elm.chat.model

import android.net.Uri
import com.tinkoff.homework.data.domain.Reaction

sealed class ChatCommand {
    data class LoadCashedData(val topicName: String, val streamId: Long) : ChatCommand()
    data class LoadData(val topicName: String, val streamId: Long) : ChatCommand()
    data class LoadImage(val uri: Uri, val topicName: String, val streamId: Long) : ChatCommand()
    data class LoadNextPage(val messageId: Long, val topicName: String, val streamId: Long) : ChatCommand()
    data class AddReaction(val messageId: Long, val reaction: Reaction) : ChatCommand()
    data class RemoveReaction(val messageId: Long, val reaction: Reaction) : ChatCommand()
    data class SendMessage(val streamId: Long, val topic: String, val message: String) :
        ChatCommand()
}