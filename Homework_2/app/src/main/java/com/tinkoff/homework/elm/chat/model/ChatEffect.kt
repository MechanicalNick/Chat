package com.tinkoff.homework.elm.chat.model

sealed class ChatEffect {
    object ScrollToLastElement : ChatEffect()
    object SmoothScrollToLastElement : ChatEffect()
    data class GoToChat(val topicName: String, val streamName: String, val streamId: Long) : ChatEffect()
}
