package com.tinkoff.homework.di.elm

import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.di.scope.ChatScope
import com.tinkoff.homework.domain.use_cases.interfaces.AddReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.ChangeReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.ChangeTopicUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.EditMessageUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetMessagesUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveMessageUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendImageUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendMessageUseCase
import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.chat.ChatActor
import com.tinkoff.homework.elm.chat.ChatReducer
import com.tinkoff.homework.elm.chat.ChatStoreFactory
import com.tinkoff.homework.elm.chat.model.ChatEffect
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.elm.chat.model.ChatState
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
        addReactionUseCase: AddReactionUseCase,
        removeReactionUseCase: RemoveReactionUseCase,
        sendMessageUseCase: SendMessageUseCase,
        changeReactionUseCase: ChangeReactionUseCase,
        removeMessageUseCase: RemoveMessageUseCase,
        editMessageUseCase: EditMessageUseCase,
        changeTopicUseCase: ChangeTopicUseCase
    ): ChatActor {
        return ChatActor(
            getMessagesUseCase,
            sendImageUseCase,
            addReactionUseCase,
            removeReactionUseCase,
            sendMessageUseCase,
            changeReactionUseCase,
            removeMessageUseCase,
            editMessageUseCase,
            changeTopicUseCase
        )
    }


    @Provides
    fun provideChatStoreFactory(
        ChatState: ChatState,
        ChatReducer: ChatReducer,
        ChatActor: ChatActor
    ): BaseStoreFactory<ChatEvent, ChatEffect, ChatState> {
        return ChatStoreFactory(ChatState, ChatReducer, ChatActor)
    }
}