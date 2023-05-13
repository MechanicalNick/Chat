package com.tinkoff.homework.di

import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.domain.repository.MessageRepository
import com.tinkoff.homework.domain.repository.PeopleRepository
import com.tinkoff.homework.domain.repository.ProfileRepository
import com.tinkoff.homework.domain.repository.StreamRepository
import com.tinkoff.homework.domain.use_cases.AddReactionUseCaseImpl
import com.tinkoff.homework.domain.use_cases.ChangeReactionUseCaseImpl
import com.tinkoff.homework.domain.use_cases.ChangeTopicUseCaseImpl
import com.tinkoff.homework.domain.use_cases.CreateStreamUseCaseImpl
import com.tinkoff.homework.domain.use_cases.EditMessageUseCaseImpl
import com.tinkoff.homework.domain.use_cases.GetMessagesUseCaseImpl
import com.tinkoff.homework.domain.use_cases.GetPeoplesUseCaseImpl
import com.tinkoff.homework.domain.use_cases.GetProfileUseCaseImpl
import com.tinkoff.homework.domain.use_cases.GetStreamsUseCaseImpl
import com.tinkoff.homework.domain.use_cases.RemoveMessageUseCaseImpl
import com.tinkoff.homework.domain.use_cases.RemoveReactionUseCaseImpl
import com.tinkoff.homework.domain.use_cases.SendImageUseCaseImpl
import com.tinkoff.homework.domain.use_cases.SendMessageUseCaseImpl
import com.tinkoff.homework.domain.use_cases.interfaces.AddReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.ChangeReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.ChangeTopicUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.CreateStreamUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.EditMessageUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetMessagesUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetPeoplesUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetProfileUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetStreamsUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveMessageUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendImageUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendMessageUseCase
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

    @Provides
    @Singleton
    fun provideCreateStreamUseCase(repository: StreamRepository): CreateStreamUseCase {
        return CreateStreamUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideChangeReactionUseCase(
        credentials: Credentials,
        addReactionUseCase: AddReactionUseCase,
        removeReactionUseCase: RemoveReactionUseCase
    ): ChangeReactionUseCase {
        return ChangeReactionUseCaseImpl(credentials, addReactionUseCase, removeReactionUseCase)
    }

    @Provides
    @Singleton
    fun provideRemoveMessageUseCase(repository: MessageRepository): RemoveMessageUseCase {
        return RemoveMessageUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideEditMessageUseCase(repository: MessageRepository): EditMessageUseCase {
        return EditMessageUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideChangeTopicUseCase(repository: MessageRepository): ChangeTopicUseCase {
        return ChangeTopicUseCaseImpl(repository)
    }
}