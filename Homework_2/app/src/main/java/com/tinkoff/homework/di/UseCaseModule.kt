package com.tinkoff.homework.di

import com.tinkoff.homework.domain.use_cases.GetProfileUseCaseImpl
import com.tinkoff.homework.domain.use_cases.interfaces.GetProfileUseCase
import com.tinkoff.homework.repository.ProfileRepository
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
}