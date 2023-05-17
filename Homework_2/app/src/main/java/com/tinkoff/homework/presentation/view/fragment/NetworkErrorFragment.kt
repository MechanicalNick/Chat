package com.tinkoff.homework.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.ErrorLayoutBinding
import com.tinkoff.homework.getAppComponent
import com.tinkoff.homework.navigation.NavigationScreens
import com.tinkoff.homework.utils.NetworkChecker
import javax.inject.Inject

class NetworkErrorFragment: Fragment() {
    @Inject
    lateinit var router: Router

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ErrorLayoutBinding.inflate(layoutInflater)
        binding.retryButton.setOnClickListener {
            if(NetworkChecker.isNetworkAvailable(this.requireActivity())) {
                router.newRootScreen(NavigationScreens.main())
            }
        }
        binding.errorText.text = getString(R.string.internet_error)
        return binding.root
    }

    override fun onAttach(context: Context) {
        context
            .getAppComponent()
            .inject(this)
        super.onAttach(context)
    }

    companion object {
        fun newInstance(): NetworkErrorFragment {
            return NetworkErrorFragment()
        }
    }
}