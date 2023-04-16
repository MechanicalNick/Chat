package com.tinkoff.homework.di.component

import android.content.Context
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.tinkoff.homework.di.*
import com.tinkoff.homework.domain.use_cases.interfaces.GetMessagesUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetPeoplesUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetProfileUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetStreamsUseCase
import com.tinkoff.homework.utils.MessageFactory
import com.tinkoff.homework.view.MainActivity
import com.tinkoff.homework.view.fragment.MainFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [LocalNavigationModule::class,
        NetworkModule::class, RepositoryModule::class, FactoryModule::class,
        UseCaseModule::class, ViewModelsModule::class]
)
interface AppComponent {
    //fun chatComponentFactory(): ChatComponent.Factory
    //fun peoplesComponentFactory(): PeoplesComponent.Factory
    //fun profileComponentFactory(): ProfileComponent.Factory
    //fun streamComponentFactory(): StreamComponent.Factory

    val router: Router
    val messageFactory: MessageFactory
    val getMessageUseCase: GetMessagesUseCase
    val getPeoplesUseCase: GetPeoplesUseCase
    val getProfileUseCase: GetProfileUseCase
    val getSteamsUseCase: GetStreamsUseCase

    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance navigatorHolder: NavigatorHolder,
            @BindsInstance router: Router
        ): AppComponent
    }
}