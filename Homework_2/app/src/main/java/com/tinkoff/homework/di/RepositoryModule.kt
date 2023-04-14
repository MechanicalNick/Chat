package com.tinkoff.homework.di

import com.tinkoff.homework.repository.MessageRepositoryImpl
import com.tinkoff.homework.repository.PeopleRepositoryImpl
import com.tinkoff.homework.repository.ProfileRepositoryImpl
import com.tinkoff.homework.repository.StreamRepositoryImpl
import com.tinkoff.homework.repository.interfaces.MessageRepository
import com.tinkoff.homework.repository.interfaces.PeopleRepository
import com.tinkoff.homework.repository.interfaces.ProfileRepository
import com.tinkoff.homework.repository.interfaces.StreamRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindsMessageRepository(impl: MessageRepositoryImpl): MessageRepository

    @Binds
    @Singleton
    fun bindsProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    fun bindsPeopleRepository(impl: PeopleRepositoryImpl): PeopleRepository

    @Binds
    @Singleton
    fun bindsStreamRepository(impl: StreamRepositoryImpl): StreamRepository
}