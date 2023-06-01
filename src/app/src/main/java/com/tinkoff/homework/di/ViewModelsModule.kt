package com.tinkoff.homework.di

import com.tinkoff.homework.presentation.viewmodel.ChannelsViewModel
import com.tinkoff.homework.presentation.viewmodel.SearchViewModel
import com.tinkoff.homework.presentation.viewmodel.ChatViewModel
import com.tinkoff.homework.presentation.viewmodel.MainViewModel
import com.tinkoff.homework.presentation.viewmodel.PeopleViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelsModule {
    @Provides
    @Singleton
    fun provideMainViewModel(): MainViewModel {
        return MainViewModel()
    }

    @Provides
    @Singleton
    fun provideChatViewModel(): ChatViewModel {
        return ChatViewModel()
    }

    @Provides
    @Singleton
    fun provideChannelViewModel() : ChannelsViewModel {
        return ChannelsViewModel()
    }

    @Provides
    @Singleton
    fun providePeopleViewModel() : PeopleViewModel {
        return PeopleViewModel()
    }
}