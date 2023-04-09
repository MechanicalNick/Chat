package com.tinkoff.homework.di

import com.tinkoff.homework.domain.use_cases.interfaces.GetProfileUseCase
import com.tinkoff.homework.elm.profile.ProfileActor
import com.tinkoff.homework.elm.profile.ProfileReducer
import com.tinkoff.homework.elm.profile.ProfileStoreFactory
import com.tinkoff.homework.elm.profile.model.ProfileState
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
    ): ProfileStoreFactory {
        return ProfileStoreFactory(profileState, profileReducer, profileActor)
    }
}
