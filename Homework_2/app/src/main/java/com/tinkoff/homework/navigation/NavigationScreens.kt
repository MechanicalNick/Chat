package com.tinkoff.homework.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.tinkoff.homework.presentation.view.fragment.ChannelsFragment
import com.tinkoff.homework.presentation.view.fragment.chat.ChatFragment
import com.tinkoff.homework.presentation.view.fragment.MainFragment
import com.tinkoff.homework.presentation.view.fragment.PeoplesFragment
import com.tinkoff.homework.presentation.view.fragment.ProfileFragment

object NavigationScreens {
    fun main() = FragmentScreen { MainFragment.newInstance() }
    fun channels() = FragmentScreen { ChannelsFragment.newInstance() }
    fun peoples() = FragmentScreen { PeoplesFragment.newInstance() }
    fun profile(userId: Long?, fromMyProfile: Boolean) = FragmentScreen("Profile_$userId") {
        ProfileFragment.newInstance(userId,  fromMyProfile)
    }
    fun chat(topicName: String, streamName: String, streamId: Long) = FragmentScreen {
        ChatFragment.newInstance(topicName, streamName, streamId)
    }
}