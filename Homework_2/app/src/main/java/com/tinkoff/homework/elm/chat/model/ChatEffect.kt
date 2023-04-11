package com.tinkoff.homework.elm.chat.model

sealed class ChatEffect {
    data class LoadError(val error: Throwable) : ChatEffect()
}
