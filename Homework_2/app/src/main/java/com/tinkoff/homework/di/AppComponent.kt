package com.tinkoff.homework.di

import com.tinkoff.homework.view.MainActivity
import com.tinkoff.homework.view.fragment.ChannelsListFragment
import com.tinkoff.homework.view.fragment.ChatFragment
import com.tinkoff.homework.view.fragment.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NavigationModule::class, LocalNavigationModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)
    fun inject(fragment: ChannelsListFragment)
    fun inject(fragment: ChatFragment)
}