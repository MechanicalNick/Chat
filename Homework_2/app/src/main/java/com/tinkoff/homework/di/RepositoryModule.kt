package com.tinkoff.homework.di

import com.tinkoff.homework.repository.*
import com.tinkoff.homework.repository.interfaces.MessageRepository
import com.tinkoff.homework.repository.interfaces.PeopleRepository
import com.tinkoff.homework.repository.interfaces.ProfileRepository
import com.tinkoff.homework.repository.interfaces.StreamRepository
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

    @Provides
    @Singleton
    fun provideProfileRepository(): ProfileRepository {
        return ProfileRepositoryImpl()
    }

    @Provides
    @Singleton
    fun providePeopleRepository(): PeopleRepository {
        return PeopleRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideStreamRepository(): StreamRepository {
        return StreamRepositoryImpl()
    }
}