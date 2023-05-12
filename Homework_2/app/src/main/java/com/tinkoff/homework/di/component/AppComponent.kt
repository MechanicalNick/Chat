package com.tinkoff.homework.di.component

import android.content.Context
import com.bumptech.glide.load.model.LazyHeaders
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.di.ApiUrlProvider
import com.tinkoff.homework.di.CredentialsModule
import com.tinkoff.homework.di.FactoryModule
import com.tinkoff.homework.di.LocalNavigationModule
import com.tinkoff.homework.di.NetworkModule
import com.tinkoff.homework.di.RepositoryModule
import com.tinkoff.homework.di.RoomModule
import com.tinkoff.homework.di.UseCaseModule
import com.tinkoff.homework.di.ViewModelsModule
import com.tinkoff.homework.domain.use_cases.interfaces.CreateStreamUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetMessagesUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetPeoplesUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetProfileUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetStreamsUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendImageUseCase
import com.tinkoff.homework.presentation.view.MessageFactory
import com.tinkoff.homework.presentation.view.StreamFactory
import com.tinkoff.homework.presentation.view.MainActivity
import com.tinkoff.homework.presentation.view.fragment.MainFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [CredentialsModule::class, LocalNavigationModule::class, RoomModule::class,
        NetworkModule::class, RepositoryModule::class, FactoryModule::class,
        UseCaseModule::class, ViewModelsModule::class]
)
interface AppComponent {
    val router: Router
    val messageFactory: MessageFactory
    val streamFactories: Map<Boolean, StreamFactory>
    val getMessageUseCase: GetMessagesUseCase
    val getPeoplesUseCase: GetPeoplesUseCase
    val getProfileUseCase: GetProfileUseCase
    val getSteamsUseCase: GetStreamsUseCase
    val sendImageUseCase: SendImageUseCase
    val createStreamUseCase: CreateStreamUseCase
    val lazyHeaders: LazyHeaders
    val credentials: Credentials
    val apiUrlProvider: ApiUrlProvider

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