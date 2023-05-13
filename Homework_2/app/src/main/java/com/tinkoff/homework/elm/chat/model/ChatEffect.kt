package com.tinkoff.homework.elm.chat.model

sealed class ChatEffect {
    object ScrollToLastElement : ChatEffect()
    object SmoothScrollToLastElement : ChatEffect()
    object ShowTimeLimitToast : ChatEffect()
    data class ShowToast(val message: String) : ChatEffect()
    data class GoToChat(val topicName: String, val streamName: String, val streamId: Long) :
        ChatEffect()
}
