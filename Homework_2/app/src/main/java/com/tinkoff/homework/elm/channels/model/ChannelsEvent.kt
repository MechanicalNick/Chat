package com.tinkoff.homework.elm.channels.model

import com.tinkoff.homework.data.domain.Stream

sealed class ChannelsEvent {

    sealed class Ui : ChannelsEvent() {
        object LoadData : Ui()

        data class Search(val query: String) : Ui()

        data class ExpandStream(val stream: Stream) : ChannelsEvent()

        data class CollapseStream(val stream: Stream) : ChannelsEvent()

        data class GoToChat(val topicName: String,
                            val streamName: String,
                            val streamId: Long) : ChannelsEvent()
    }

    sealed class Internal : ChannelsEvent() {

        data class DataLoaded(val streams: List<Stream>) : Internal()

        data class ErrorLoading(val error: Throwable) : Internal()
    }
}