package com.tinkoff.homework.presentation.viewmodel

import com.tinkoff.homework.elm.channels.model.ChannelsEffect
import com.tinkoff.homework.elm.channels.model.ChannelsEvent
import com.tinkoff.homework.elm.channels.model.ChannelsState

class ChannelsViewModel : SearchViewModel<ChannelsEvent, ChannelsEffect, ChannelsState>() {
    override fun accept(searchQuery: String) {
        store?.accept(ChannelsEvent.Ui.Search(searchQuery))
    }
}