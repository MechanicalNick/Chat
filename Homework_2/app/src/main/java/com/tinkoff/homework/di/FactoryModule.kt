package com.tinkoff.homework.di

import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.domain.use_cases.interfaces.AddReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendMessageUseCase
import com.tinkoff.homework.utils.MessageFactory
import com.tinkoff.homework.utils.StreamFactory
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
class FactoryModule {
    @Singleton
    @Provides
    fun providesMessageFactory(
        addReactionUseCase: AddReactionUseCase,
        removeReactionUseCase: RemoveReactionUseCase,
        sendMessageUseCase: SendMessageUseCase,
        credentials: Credentials
    ): MessageFactory {
        return MessageFactory(
            addReactionUseCase,
            removeReactionUseCase,
            sendMessageUseCase,
            credentials
        )
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