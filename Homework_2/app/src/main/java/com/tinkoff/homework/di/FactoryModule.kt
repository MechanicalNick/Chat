package com.tinkoff.homework.di

import com.tinkoff.homework.domain.use_cases.interfaces.AddReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendMessageUseCase
import com.tinkoff.homework.utils.MessageFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FactoryModule {
    @Singleton
    @Provides
    fun providesMessageFactory(
        addReactionUseCase: AddReactionUseCase,
        removeReactionUseCase: RemoveReactionUseCase,
        sendMessageUseCase: SendMessageUseCase
    ): MessageFactory {
        return MessageFactory(addReactionUseCase, removeReactionUseCase, sendMessageUseCase)
    }
}