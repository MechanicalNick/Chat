package com.tinkoff.homework.elm.channels

import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.channels.model.ChannelsEffect
import com.tinkoff.homework.elm.channels.model.ChannelsEvent
import com.tinkoff.homework.elm.channels.model.ChannelsState
import com.tinkoff.homework.elm.people.model.PeopleEffect
import com.tinkoff.homework.elm.people.model.PeopleEvent
import com.tinkoff.homework.elm.people.model.PeopleState
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.rx2.ElmStoreCompat

class ChannelsStoreFactory(
    private val channelsState: ChannelsState,
    private val channelsReducer: ChannelsReducer,
    private val actor: ChannelsActor,
    private val onlySubscribed: Boolean
): BaseStoreFactory<ChannelsEvent, ChannelsEffect, ChannelsState> {
    private val allStore by lazy {
        ElmStoreCompat(
            initialState = channelsState.copy(onlySubscribed = false),
            reducer = channelsReducer,
            actor = actor
        )
    }
    private val subscribedStore by lazy {
        ElmStoreCompat(
            initialState = channelsState.copy(onlySubscribed = true),
            reducer = channelsReducer,
            actor = actor
        )
    }

    fun provide() = if (onlySubscribed) subscribedStore else allStore
    override fun currentStore(): Store<ChannelsEvent, ChannelsEffect, ChannelsState> {
        return provide()
    }
}