package com.tinkoff.homework.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.tinkoff.homework.presentation.view.fragment.ChannelsFragment
import com.tinkoff.homework.presentation.view.fragment.ChatFragment
import com.tinkoff.homework.presentation.view.fragment.MainFragment
import com.tinkoff.homework.presentation.view.fragment.PeoplesFragment
import com.tinkoff.homework.presentation.view.fragment.ProfileFragment
import com.tinkoff.homework.view.fragment.*

object NavigationScreens {
    fun main() = FragmentScreen { MainFragment.newInstance() }
    fun channels() = FragmentScreen { ChannelsFragment.newInstance() }
    fun peoples() = FragmentScreen { PeoplesFragment.newInstance() }
    fun profile(userId: Long?) = FragmentScreen("Profile_$userId") {
        ProfileFragment.newInstance(userId)
    }

    fun chat(topicName: String, streamName: String, streamId: Long) = FragmentScreen {
        ChatFragment.newInstance(topicName, streamName, streamId)
    }
}