package com.tinkoff.homework.di.component

import com.tinkoff.homework.di.elm.StreamModule
import com.tinkoff.homework.di.scope.StreamScope
import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.channels.ChannelsActor
import com.tinkoff.homework.elm.channels.ChannelsReducer
import com.tinkoff.homework.elm.channels.model.ChannelsEffect
import com.tinkoff.homework.elm.channels.model.ChannelsEvent
import com.tinkoff.homework.elm.channels.model.ChannelsState
import com.tinkoff.homework.presentation.view.fragment.ChannelsListFragment
import dagger.Component

@StreamScope
@Component(modules = [StreamModule::class], dependencies = [AppComponent::class])
interface StreamComponent {
    val channelsStoreFactories: Map<Boolean, BaseStoreFactory<ChannelsEvent, ChannelsEffect, ChannelsState>>

    fun inject(fragment: ChannelsListFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): StreamComponent
    }
}