package com.tinkoff.homework

import android.app.Application
import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.tinkoff.homework.di.component.AppComponent
import com.tinkoff.homework.di.component.DaggerAppComponent

class App: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        val cicerone = Cicerone.create()
        appComponent = DaggerAppComponent.factory()
            .create(this, cicerone.getNavigatorHolder(), cicerone.router)
    }
}

fun Context.getAppComponent(): AppComponent = (this.applicationContext as App).appComponent