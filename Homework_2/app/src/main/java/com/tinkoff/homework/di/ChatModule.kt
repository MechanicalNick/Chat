package com.tinkoff.homework.di

import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.di.scope.ChatScope
import com.tinkoff.homework.domain.use_cases.interfaces.GetMessagesUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendImageUseCase
import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.chat.ChatActor
import com.tinkoff.homework.elm.chat.ChatReducer
import com.tinkoff.homework.elm.chat.ChatStoreFactory
import com.tinkoff.homework.elm.chat.model.ChatEffect
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.elm.chat.model.ChatState
import com.tinkoff.homework.navigation.MessageFactory
import dagger.Module
import dagger.Provides

@Module
class ChatModule {
    @ChatScope
    @Provides
    fun provideChatState(): ChatState {
        return ChatState()
    }

    @ChatScope
    @Provides
    fun provideChatReducer(credentials: Credentials): ChatReducer {
        return ChatReducer(credentials)
    }

    @ChatScope
    @Provides
    fun provideChatActor(
        getMessagesUseCase: GetMessagesUseCase,
        sendImageUseCase: SendImageUseCase,
        messageFactory: MessageFactory
    ): ChatActor {
        return ChatActor(getMessagesUseCase, sendImageUseCase, messageFactory)
    }

    @ChatScope
    @Provides
    fun provideChatStoreFactory(
        ChatState: ChatState,
        ChatReducer: ChatReducer,
        ChatActor: ChatActor
    ): BaseStoreFactory<ChatEvent, ChatEffect, ChatState> {
        return ChatStoreFactory(ChatState, ChatReducer, ChatActor)
    }
}