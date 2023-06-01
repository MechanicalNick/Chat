package com.tinkoff.homework.di

import com.tinkoff.homework.data.repository.MessageRepositoryImpl
import com.tinkoff.homework.data.repository.PeopleRepositoryImpl
import com.tinkoff.homework.data.repository.ProfileRepositoryImpl
import com.tinkoff.homework.data.repository.StreamRepositoryImpl
import com.tinkoff.homework.domain.repository.MessageRepository
import com.tinkoff.homework.domain.repository.PeopleRepository
import com.tinkoff.homework.domain.repository.ProfileRepository
import com.tinkoff.homework.domain.repository.StreamRepository
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