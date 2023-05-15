package com.tinkoff.homework.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.MainFragmentBinding
import com.tinkoff.homework.getAppComponent
import com.tinkoff.homework.navigation.LocalCiceroneHolder
import com.tinkoff.homework.navigation.NavigationScreens
import com.tinkoff.homework.presentation.viewmodel.MainViewModel
import javax.inject.Inject

class MainFragment: Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val navigator: Navigator by lazy {
        AppNavigator(requireActivity(), R.id.fragment_container_view, childFragmentManager)
    }
    private val containerName = "MainFragment"
    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerName)

    @Inject
    lateinit var ciceroneHolder: LocalCiceroneHolder

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onAttach(context: Context) {
        context
            .getAppComponent()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(layoutInflater)

        setupNavigationBar()

        if(!mainViewModel.isSavedState)
            cicerone.router.replaceScreen(NavigationScreens.channels())

        mainViewModel.isSavedState = true

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupNavigationBar() {
        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_channels ->
                    cicerone.router.navigateTo(NavigationScreens.channels())
                R.id.navigation_people ->
                    cicerone.router.navigateTo(NavigationScreens.peoples())
                R.id.navigation_profile ->
                    cicerone.router.navigateTo(NavigationScreens.profile(null))
                else -> throw NotImplementedError("setupNavigationBar with ${item.itemId}")
            }
            true
        }
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}