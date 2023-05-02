package com.tinkoff.homework.elm.chat.model

sealed class ChatEffect {
    object ScrollToLastElement : ChatEffect()
    object SmoothScrollToLastElement : ChatEffect()
}
