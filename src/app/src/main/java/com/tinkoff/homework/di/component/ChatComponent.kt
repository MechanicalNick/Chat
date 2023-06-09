package com.tinkoff.homework.di.component

import com.tinkoff.homework.di.elm.ChatModule
import com.tinkoff.homework.di.scope.ChatScope
import com.tinkoff.homework.presentation.view.fragment.chat.ChatFragment
import dagger.Component

@ChatScope
@Component(modules = [ChatModule::class], dependencies = [AppComponent::class])
interface ChatComponent {
    fun inject(fragment: ChatFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): ChatComponent
    }
}