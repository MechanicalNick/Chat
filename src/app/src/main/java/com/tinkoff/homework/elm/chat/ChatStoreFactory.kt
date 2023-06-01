package com.tinkoff.homework.elm.chat

import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.chat.model.ChatEffect
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.elm.chat.model.ChatState
import vivid.money.elmslie.rx2.ElmStoreCompat

class ChatStoreFactory(
    private val ChatState: ChatState,
    private val ChatReducer: ChatReducer,
    private val actor: ChatActor
) : BaseStoreFactory<ChatEvent, ChatEffect, ChatState> {
    private val store by lazy {
        ElmStoreCompat(
            initialState = ChatState,
            reducer = ChatReducer,
            actor = actor
        )
    }

    fun provide() = store
    override fun currentStore() = store
}