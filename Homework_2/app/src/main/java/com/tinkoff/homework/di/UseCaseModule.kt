package com.tinkoff.homework.di

import com.tinkoff.homework.domain.use_cases.GetPeoplesUseCaseImpl
import com.tinkoff.homework.domain.use_cases.GetProfileUseCaseImpl
import com.tinkoff.homework.domain.use_cases.GetStreamsUseCaseImpl
import com.tinkoff.homework.domain.use_cases.interfaces.GetPeoplesUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetProfileUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetStreamsUseCase
import com.tinkoff.homework.repository.PeopleRepository
import com.tinkoff.homework.repository.ProfileRepository
import com.tinkoff.homework.repository.StreamRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {
    @Provides
    @Singleton
    fun provideGetProfileUseCase(profileRepository: ProfileRepository): GetProfileUseCase {
        return GetProfileUseCaseImpl(profileRepository)
    }

    @Provides
    @Singleton
    fun provideGetPeoplesUseCase(peopleRepository: PeopleRepository): GetPeoplesUseCase {
        return GetPeoplesUseCaseImpl(peopleRepository)
    }

    @Provides
    @Singleton
    fun provideGetStreamsUseCase(streamRepository: StreamRepository): GetStreamsUseCase {
        return GetStreamsUseCaseImpl(streamRepository)
    }
}