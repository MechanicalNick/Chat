package com.tinkoff.homework.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.tinkoff.homework.view.fragment.*

object NavigationScreens {
    fun main() = FragmentScreen { MainFragment.newInstance() }
    fun channels() = FragmentScreen { ChannelsFragment.newInstance() }
    fun peoples() = FragmentScreen { PeoplesFragment.newInstance() }
    fun profile(userId: Int) = FragmentScreen("Profile_$userId") {
        ProfileFragment.newInstance(userId)
    }
    fun chat(id: Int, chatName: String) = FragmentScreen { ChatFragment.newInstance(id, chatName)}
}