package com.tinkoff.homework.elm.channels

import com.tinkoff.homework.elm.channels.model.ChannelsState
import vivid.money.elmslie.rx2.ElmStoreCompat

class ChannelsStoreFactory(
    private val ChannelsState: ChannelsState,
    private val ChannelsReducer: ChannelsReducer,
    private val actor: ChannelsActor
) {
    private val allStore by lazy {
        ElmStoreCompat(
            initialState = ChannelsState.copy(onlySubscribed = false),
            reducer = ChannelsReducer,
            actor = actor
        )
    }
    private val subscribedStore by lazy {
        ElmStoreCompat(
            initialState = ChannelsState.copy(onlySubscribed = true),
            reducer = ChannelsReducer,
            actor = actor
        )
    }

    fun provide(onlySubscribed: Boolean) = if (onlySubscribed) subscribedStore else allStore
}