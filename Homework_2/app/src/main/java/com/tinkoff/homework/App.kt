package com.tinkoff.homework

import android.app.Application
import com.tinkoff.homework.di.AppComponent
import com.tinkoff.homework.di.DaggerAppComponent

class App: Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().build()
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE: App
    }
}