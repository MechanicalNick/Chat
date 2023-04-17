package com.tinkoff.homework.di

import com.tinkoff.homework.repository.*
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