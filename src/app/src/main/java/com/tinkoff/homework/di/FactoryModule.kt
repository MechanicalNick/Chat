package com.tinkoff.homework.di

import com.tinkoff.homework.presentation.view.MessageFactory
import com.tinkoff.homework.presentation.view.StreamFactory
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
class FactoryModule {
    @Singleton
    @Provides
    fun providesMessageFactory(): MessageFactory {
        return MessageFactory()
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