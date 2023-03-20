package com.tinkoff.homework.di

import com.tinkoff.homework.view.ChannelsListFragment
import com.tinkoff.homework.view.MainActivity
import com.tinkoff.homework.view.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NavigationModule::class, LocalNavigationModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)
    fun inject(fragment: ChannelsListFragment)
}