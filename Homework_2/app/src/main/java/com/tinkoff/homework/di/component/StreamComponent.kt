package com.tinkoff.homework.di.component

import com.tinkoff.homework.di.StreamModule
import com.tinkoff.homework.di.scope.StreamScope
import com.tinkoff.homework.view.fragment.ChannelsListFragment
import dagger.Component

@StreamScope
@Component(modules = [StreamModule::class], dependencies = [AppComponent::class])
interface StreamComponent {
    fun inject(fragment: ChannelsListFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): StreamComponent
    }
}