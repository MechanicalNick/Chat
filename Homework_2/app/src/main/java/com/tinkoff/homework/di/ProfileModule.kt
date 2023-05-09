package com.tinkoff.homework.di

import com.tinkoff.homework.di.scope.ProfileScope
import com.tinkoff.homework.domain.use_cases.interfaces.GetProfileUseCase
import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.profile.ProfileActor
import com.tinkoff.homework.elm.profile.ProfileReducer
import com.tinkoff.homework.elm.profile.ProfileStoreFactory
import com.tinkoff.homework.elm.profile.model.ProfileEffect
import com.tinkoff.homework.elm.profile.model.ProfileEvent
import com.tinkoff.homework.elm.profile.model.ProfileState
import dagger.Module
import dagger.Provides

@Module
class ProfileModule {

    @ProfileScope
    @Provides
    fun provideProfileState(): ProfileState {
        return ProfileState()
    }

    @ProfileScope
    @Provides
    fun provideProfileReducer(): ProfileReducer {
        return ProfileReducer()
    }

    @ProfileScope
    @Provides
    fun provideProfileActor(getProfileUseCase: GetProfileUseCase): ProfileActor {
        return ProfileActor(getProfileUseCase)
    }

    @ProfileScope
    @Provides
    fun provideProfileStoreFactory(
        profileState: ProfileState,
        profileReducer: ProfileReducer,
        profileActor: ProfileActor
    ): BaseStoreFactory<ProfileEvent, ProfileEffect, ProfileState> {
        return ProfileStoreFactory(profileState, profileReducer, profileActor)
    }
}