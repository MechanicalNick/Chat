package com.tinkoff.homework.elm.channels.model

sealed class ChannelsEffect {
    data class LoadError(val error: Throwable) : ChannelsEffect()
    data class GoToChat(val topicName: String, val streamName: String, val streamId: Long) : ChannelsEffect()
}
