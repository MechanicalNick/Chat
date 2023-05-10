package com.tinkoff.homework.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.ActivityMainBinding
import com.tinkoff.homework.getAppComponent
import com.tinkoff.homework.navigation.NavigationScreens
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    @Inject
    lateinit var router: Router

    private val navigator = AppNavigator(this, R.id.main_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        this.applicationContext
            .getAppComponent()
            .inject(this)

        RxJavaPlugins.setErrorHandler { e ->
            if (e is UndeliverableException) {
                Log.e("RxJava", e.cause?.message.orEmpty())
            } else {
                Thread.currentThread().also { thread ->
                    thread.uncaughtExceptionHandler.uncaughtException(thread, e)
                }
            }
        }

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (savedInstanceState == null)
            router.newRootScreen(NavigationScreens.main())
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        router.exit()
    }
}