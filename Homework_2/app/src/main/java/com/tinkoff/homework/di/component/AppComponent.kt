package com.tinkoff.homework.di.component

import android.content.Context
import com.bumptech.glide.load.model.LazyHeaders
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.di.*
import com.tinkoff.homework.domain.use_cases.interfaces.*
import com.tinkoff.homework.utils.MessageFactory
import com.tinkoff.homework.utils.StreamFactory
import com.tinkoff.homework.view.MainActivity
import com.tinkoff.homework.view.fragment.MainFragment
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
    val lazyHeaders: LazyHeaders
    val credentials: Credentials

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