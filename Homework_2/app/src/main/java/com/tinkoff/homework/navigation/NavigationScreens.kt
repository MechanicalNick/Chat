package com.tinkoff.homework.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.tinkoff.homework.view.ChannelsFragment
import com.tinkoff.homework.view.PeoplesFragment
import com.tinkoff.homework.view.ProfileFragment

object NavigationScreens {
    fun channels() = FragmentScreen { ChannelsFragment.newInstance() }
    fun peoples() = FragmentScreen { PeoplesFragment() }
    fun profile(userId: Long) = FragmentScreen("Profile_$userId") { ProfileFragment(userId) }
}