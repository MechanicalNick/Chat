package com.tinkoff.homework.di.component

import com.tinkoff.homework.di.elm.PeoplesModule
import com.tinkoff.homework.di.scope.PeoplesScope
import com.tinkoff.homework.presentation.view.fragment.PeoplesFragment
import dagger.Component

@PeoplesScope
@Component(modules = [PeoplesModule::class], dependencies = [AppComponent::class])
interface PeoplesComponent {
    fun inject(fragment: PeoplesFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): PeoplesComponent
    }
}