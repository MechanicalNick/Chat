package com.tinkoff.homework.di

import com.tinkoff.homework.di.scope.StreamScope
import com.tinkoff.homework.domain.use_cases.interfaces.GetStreamsUseCase
import com.tinkoff.homework.elm.channels.ChannelsActor
import com.tinkoff.homework.elm.channels.ChannelsReducer
import com.tinkoff.homework.elm.channels.ChannelsStoreFactory
import com.tinkoff.homework.elm.channels.model.ChannelsState
import dagger.Module
import dagger.Provides

@Module
class StreamModule {

    @StreamScope
    @Provides
    fun provideChannelsState(): ChannelsState {
        return ChannelsState()
    }

    @StreamScope
    @Provides
    fun provideChannelsReducer(): ChannelsReducer {
        return ChannelsReducer()
    }

    @StreamScope
    @Provides
    fun provideChannelsActor(getStreamsUseCase: GetStreamsUseCase): ChannelsActor {
        return ChannelsActor(getStreamsUseCase)
    }

    @StreamScope
    @Provides
    fun provideChannelsStoreFactory(
        ChannelsState: ChannelsState,
        ChannelsReducer: ChannelsReducer,
        ChannelsActor: ChannelsActor
    ): ChannelsStoreFactory {
        return ChannelsStoreFactory(ChannelsState, ChannelsReducer, ChannelsActor)
    }
}