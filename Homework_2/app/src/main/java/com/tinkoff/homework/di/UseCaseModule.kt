package com.tinkoff.homework.di

import com.tinkoff.homework.domain.use_cases.*
import com.tinkoff.homework.domain.use_cases.interfaces.*
import com.tinkoff.homework.repository.interfaces.MessageRepository
import com.tinkoff.homework.repository.interfaces.PeopleRepository
import com.tinkoff.homework.repository.interfaces.ProfileRepository
import com.tinkoff.homework.repository.interfaces.StreamRepository
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

    @Provides
    @Singleton
    fun provideAddReactionUseCase(repository: MessageRepository): AddReactionUseCase {
        return AddReactionUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideRemoveReactionUseCase(repository: MessageRepository): RemoveReactionUseCase {
        return RemoveReactionUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideSendMessageUseCase(repository: MessageRepository): SendMessageUseCase {
        return SendMessageUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGetMessagesUseCase(repository: MessageRepository): GetMessagesUseCase {
        return GetMessagesUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideSendImageUseCase(repository: MessageRepository): SendImageUseCase {
        return SendImageUseCaseImpl(repository)
    }
}