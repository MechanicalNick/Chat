package com.tinkoff.homework.viewmodel

import androidx.lifecycle.ViewModel
import com.tinkoff.homework.elm.chat.model.ChatEffect
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.elm.chat.model.ChatState
import vivid.money.elmslie.core.store.Store

class ChatViewModel : ViewModel() {
    lateinit var store: Store<ChatEvent, ChatEffect, ChatState>
}