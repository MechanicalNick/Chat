package com.tinkoff.homework.di

import com.tinkoff.homework.domain.use_cases.interfaces.GetMessagesUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetPeoplesUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetProfileUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetStreamsUseCase
import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.channels.ChannelsActor
import com.tinkoff.homework.elm.channels.ChannelsReducer
import com.tinkoff.homework.elm.channels.ChannelsStoreFactory
import com.tinkoff.homework.elm.channels.model.ChannelsState
import com.tinkoff.homework.elm.chat.ChatActor
import com.tinkoff.homework.elm.chat.ChatReducer
import com.tinkoff.homework.elm.chat.ChatStoreFactory
import com.tinkoff.homework.elm.chat.model.ChatEffect
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.elm.chat.model.ChatState
import com.tinkoff.homework.elm.people.PeopleActor
import com.tinkoff.homework.elm.people.PeopleReducer
import com.tinkoff.homework.elm.people.PeopleStoreFactory
import com.tinkoff.homework.elm.people.model.PeopleEffect
import com.tinkoff.homework.elm.people.model.PeopleEvent
import com.tinkoff.homework.elm.people.model.PeopleState
import com.tinkoff.homework.elm.profile.ProfileActor
import com.tinkoff.homework.elm.profile.ProfileReducer
import com.tinkoff.homework.elm.profile.ProfileStoreFactory
import com.tinkoff.homework.elm.profile.model.ProfileEffect
import com.tinkoff.homework.elm.profile.model.ProfileEvent
import com.tinkoff.homework.elm.profile.model.ProfileState
import com.tinkoff.homework.utils.MessageFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ElmModule {
    @Provides
    @Singleton
    fun provideProfileState(): ProfileState {
        return ProfileState()
    }

    @Provides
    @Singleton
    fun provideProfileReducer(): ProfileReducer {
        return ProfileReducer()
    }

    @Provides
    @Singleton
    fun provideProfileActor(getProfileUseCase: GetProfileUseCase): ProfileActor {
        return ProfileActor(getProfileUseCase)
    }

    @Provides
    @Singleton
    fun provideProfileStoreFactory(
        profileState: ProfileState,
        profileReducer: ProfileReducer,
        profileActor: ProfileActor
    ): BaseStoreFactory<ProfileEvent, ProfileEffect, ProfileState> {
        return ProfileStoreFactory(profileState, profileReducer, profileActor)
    }

    @Provides
    @Singleton
    fun providePeopleState(): PeopleState {
        return PeopleState()
    }

    @Provides
    @Singleton
    fun providePeopleReducer(): PeopleReducer {
        return PeopleReducer()
    }

    @Provides
    @Singleton
    fun providePeopleActor(getPeopleUseCase: GetPeoplesUseCase): PeopleActor {
        return PeopleActor(getPeopleUseCase)
    }

    @Provides
    @Singleton
    fun providePeopleStoreFactory(
        peopleState: PeopleState,
        peopleReducer: PeopleReducer,
        peopleActor: PeopleActor
    ): BaseStoreFactory<PeopleEvent, PeopleEffect, PeopleState> {
        return PeopleStoreFactory(peopleState, peopleReducer, peopleActor)
    }

    @Provides
    @Singleton
    fun provideChannelsState(): ChannelsState {
        return ChannelsState()
    }

    @Provides
    @Singleton
    fun provideChannelsReducer(): ChannelsReducer {
        return ChannelsReducer()
    }

    @Provides
    @Singleton
    fun provideChannelsActor(getStreamsUseCase: GetStreamsUseCase): ChannelsActor {
        return ChannelsActor(getStreamsUseCase)
    }

    @Provides
    @Singleton
    fun provideChannelsStoreFactory(
        ChannelsState: ChannelsState,
        ChannelsReducer: ChannelsReducer,
        ChannelsActor: ChannelsActor
    ): ChannelsStoreFactory {
        return ChannelsStoreFactory(ChannelsState, ChannelsReducer, ChannelsActor)
    }

    @Provides
    @Singleton
    fun provideChatState(): ChatState {
        return ChatState()
    }

    @Provides
    @Singleton
    fun provideChatReducer(): ChatReducer {
        return ChatReducer()
    }

    @Provides
    @Singleton
    fun provideChatActor(
        getMessagesUseCase: GetMessagesUseCase,
        messageFactory: MessageFactory
    ): ChatActor {
        return ChatActor(getMessagesUseCase, messageFactory)
    }

    @Provides
    @Singleton
    fun provideChatStoreFactory(
        ChatState: ChatState,
        ChatReducer: ChatReducer,
        ChatActor: ChatActor
    ): BaseStoreFactory<ChatEvent, ChatEffect, ChatState> {
        return ChatStoreFactory(ChatState, ChatReducer, ChatActor)
    }
}
