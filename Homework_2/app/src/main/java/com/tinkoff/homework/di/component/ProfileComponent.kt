package com.tinkoff.homework.di.component

import com.tinkoff.homework.di.ProfileModule
import com.tinkoff.homework.di.scope.ProfileScope
import com.tinkoff.homework.presentation.view.fragment.ProfileFragment
import dagger.Component

@ProfileScope
@Component(modules = [ProfileModule::class], dependencies = [AppComponent::class])
interface ProfileComponent {
    fun inject(fragment: ProfileFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): ProfileComponent
    }
}