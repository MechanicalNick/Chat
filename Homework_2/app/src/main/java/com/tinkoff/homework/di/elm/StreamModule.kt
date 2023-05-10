package com.tinkoff.homework.di.elm

import com.tinkoff.homework.di.BooleanKey
import com.tinkoff.homework.di.scope.StreamScope
import com.tinkoff.homework.domain.use_cases.interfaces.GetStreamsUseCase
import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.channels.ChannelsActor
import com.tinkoff.homework.elm.channels.ChannelsReducer
import com.tinkoff.homework.elm.channels.ChannelsStoreFactory
import com.tinkoff.homework.elm.channels.model.ChannelsEffect
import com.tinkoff.homework.elm.channels.model.ChannelsEvent
import com.tinkoff.homework.elm.channels.model.ChannelsState
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

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
    @IntoMap
    @BooleanKey(true)
    fun provideOnlySubscribedChannelsStoreFactory(
        channelsState: ChannelsState,
        channelsReducer: ChannelsReducer,
        channelsActor: ChannelsActor,
    ): BaseStoreFactory<ChannelsEvent, ChannelsEffect, ChannelsState> {
        return ChannelsStoreFactory(
            channelsState,
            channelsReducer,
            channelsActor,
            onlySubscribed = true
        )
    }

    @StreamScope
    @Provides
    @IntoMap
    @BooleanKey(false)
    fun provideAllChannelsStoreFactory(
        channelsState: ChannelsState,
        channelsReducer: ChannelsReducer,
        channelsActor: ChannelsActor,
    ): BaseStoreFactory<ChannelsEvent, ChannelsEffect, ChannelsState>{
        return ChannelsStoreFactory(
            channelsState,
            channelsReducer,
            channelsActor,
            onlySubscribed = false
        )
    }
}