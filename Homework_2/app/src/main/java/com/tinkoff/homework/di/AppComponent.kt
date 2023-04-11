package com.tinkoff.homework.di

import com.tinkoff.homework.repository.MessageRepositoryImpl
import com.tinkoff.homework.repository.PeopleRepositoryImpl
import com.tinkoff.homework.repository.ProfileRepositoryImpl
import com.tinkoff.homework.repository.StreamRepositoryImpl
import com.tinkoff.homework.view.MainActivity
import com.tinkoff.homework.view.fragment.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [NavigationModule::class, LocalNavigationModule::class,
        NetworkModule::class, RepositoryModule::class, FactoryModule::class,
        UseCaseModule::class, ElmModule::class, ViewModelsModule::class]
)
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)
    fun inject(fragment: ChannelsListFragment)
    fun inject(fragment: ChatFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: PeoplesFragment)
    fun inject(repository: StreamRepositoryImpl)
    fun inject(repository: ProfileRepositoryImpl)
    fun inject(repository: PeopleRepositoryImpl)
    fun inject(repository: MessageRepositoryImpl)
}