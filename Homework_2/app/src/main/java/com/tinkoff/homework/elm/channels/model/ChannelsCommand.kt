package com.tinkoff.homework.elm.channels.model

sealed class ChannelsCommand {
    data class LoadData(val isSubscribed: Boolean) : ChannelsCommand()
    data class Search(val isSubscribed: Boolean, val query: String) : ChannelsCommand()
}