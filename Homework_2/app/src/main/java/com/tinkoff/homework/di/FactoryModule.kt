package com.tinkoff.homework.di

import com.tinkoff.homework.di.scope.StreamScope
import com.tinkoff.homework.domain.use_cases.interfaces.AddReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendMessageUseCase
import com.tinkoff.homework.utils.MessageFactory
import com.tinkoff.homework.utils.StreamFactory
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
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

    @Singleton
    @IntoMap
    @BooleanKey(true)
    @Provides
    fun provideStreamFactoryOnlySubscribed(): StreamFactory {
        return StreamFactory()
    }

    @Singleton
    @IntoMap
    @BooleanKey(false)
    @Provides
    fun provideStreamFactoryAll(): StreamFactory {
        return StreamFactory()
    }
}