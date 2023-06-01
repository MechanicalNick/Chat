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
import com.tinkoff.homework.domain.use_cases.interfaces.AddReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.ChangeReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.ChangeTopicUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.CreateStreamUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.EditMessageUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetMessagesUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetPeoplesUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetProfileUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.GetStreamsUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveMessageUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendImageUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendMessageUseCase
import com.tinkoff.homework.presentation.view.MainActivity
import com.tinkoff.homework.presentation.view.MessageFactory
import com.tinkoff.homework.presentation.view.StreamFactory
import com.tinkoff.homework.presentation.view.fragment.ActionSelectorFragment
import com.tinkoff.homework.presentation.view.fragment.MainFragment
import com.tinkoff.homework.presentation.view.fragment.NetworkErrorFragment
import com.tinkoff.homework.presentation.view.fragment.ReactionFragment
import com.tinkoff.homework.presentation.viewmodel.ChannelsViewModel
import com.tinkoff.homework.presentation.viewmodel.SearchViewModel
import com.tinkoff.homework.presentation.viewmodel.ChatViewModel
import com.tinkoff.homework.presentation.viewmodel.PeopleViewModel
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
    val lazyHeaders: LazyHeaders
    val credentials: Credentials
    val apiUrlProvider: ApiUrlProvider
    val chatViewModel: ChatViewModel
    val channelViewModel: ChannelsViewModel
    val peopleViewModel: PeopleViewModel

    val getMessageUseCase: GetMessagesUseCase
    val getPeoplesUseCase: GetPeoplesUseCase
    val getProfileUseCase: GetProfileUseCase
    val getSteamsUseCase: GetStreamsUseCase
    val sendImageUseCase: SendImageUseCase
    val addReactionUseCase: AddReactionUseCase
    val removeReactionUseCase: RemoveReactionUseCase
    val sendMessageUseCase: SendMessageUseCase
    val changeReactionUseCase: ChangeReactionUseCase
    val removeMessageUseCase: RemoveMessageUseCase
    val createStreamUseCase: CreateStreamUseCase
    val editMessageUseCase: EditMessageUseCase
    val changeTopicUseCase: ChangeTopicUseCase

    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)
    fun inject(fragment: ActionSelectorFragment)
    fun inject(fragment: ReactionFragment)
    fun inject(fragment: NetworkErrorFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance navigatorHolder: NavigatorHolder,
            @BindsInstance router: Router
        ): AppComponent
    }
}