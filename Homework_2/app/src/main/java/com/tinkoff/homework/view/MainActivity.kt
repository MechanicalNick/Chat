package com.tinkoff.homework.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.ActivityMainBinding
import com.tinkoff.homework.navigation.NavigationScreens


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var navigatorHolder: NavigatorHolder

    private val myId = 1L
    private val localCicerone = Cicerone.create()
    private val navigator = AppNavigator(this, R.id.fragment_container_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigatorHolder = localCicerone.getNavigatorHolder()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        setupNavigationBar()
        localCicerone.router.replaceScreen(NavigationScreens.channels())
        setContentView(binding.root)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun setupNavigationBar() {
        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_channels ->
                    localCicerone.router.replaceScreen(NavigationScreens.channels())
                R.id.navigation_people ->
                    localCicerone.router.replaceScreen(NavigationScreens.peoples())
                R.id.navigation_profile ->
                    localCicerone.router.replaceScreen(NavigationScreens.profile(myId))
                else -> throw NotImplementedError("setupNavigationBar with ${item.itemId}")
            }
            true
        }
    }
}