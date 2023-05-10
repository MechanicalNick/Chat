package com.tinkoff.homework.di

import com.tinkoff.homework.presentation.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelsModule {
    @Provides
    @Singleton
    fun provideMainViewModel(): MainViewModel {
        return  MainViewModel()
    }
}