package com.tinkoff.homework.di

import com.tinkoff.homework.repository.MessageRepository
import com.tinkoff.homework.repository.MessageRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMessageRepository(): MessageRepository {
        return MessageRepositoryImpl()
    }
}