package com.tinkoff.homework.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.tinkoff.homework.App
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.MainFragmentBinding
import com.tinkoff.homework.navigation.LocalCiceroneHolder
import com.tinkoff.homework.navigation.NavigationScreens
import javax.inject.Inject

class MainFragment private constructor(): Fragment() {
    @Inject
    lateinit var ciceroneHolder: LocalCiceroneHolder
    lateinit var binding: MainFragmentBinding

    private val myId = 1L
    private val navigator: Navigator by lazy {
        AppNavigator(requireActivity(), R.id.fragment_container_view, childFragmentManager)
    }
    private val containerName = "MainFragment"
    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerName)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        App.INSTANCE.appComponent.inject(this)

        binding = MainFragmentBinding.inflate(layoutInflater)

        setupNavigationBar()

        cicerone.router.replaceScreen(NavigationScreens.channels())

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        cicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }


    private fun setupNavigationBar() {
        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_channels ->
                    cicerone.router.navigateTo(NavigationScreens.channels())
                R.id.navigation_people ->
                    cicerone.router.navigateTo(NavigationScreens.peoples())
                R.id.navigation_profile ->
                    cicerone.router.navigateTo(NavigationScreens.profile(myId))
                else -> throw NotImplementedError("setupNavigationBar with ${item.itemId}")
            }
            true
        }
    }

    companion object {
        private const val ARG_MESSAGE = "main"
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}