package com.tinkoff.homework.presentation.view.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.FragmentChannelBinding
import com.tinkoff.homework.databinding.LayoutCreateChannelBinding
import com.tinkoff.homework.di.component.DaggerStreamComponent
import com.tinkoff.homework.domain.use_cases.interfaces.CreateStreamUseCase
import com.tinkoff.homework.getAppComponent
import com.tinkoff.homework.presentation.ChannelPagerAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject


class ChannelsFragment: Fragment()  {
    @Inject
    lateinit var createStreamUseCase: CreateStreamUseCase
    private lateinit var binding: FragmentChannelBinding
    private var ignoreRotation: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChannelBinding.inflate(layoutInflater)
        ignoreRotation = true

        val tabNames: List<String> = listOf(getString(R.string.subscribed), getString(R.string.all_stream))
        val pagerAdapter = ChannelPagerAdapter(this, getTabs())

        with(binding.layoutChannel) {
            fragmentViewPager.adapter = pagerAdapter

            TabLayoutMediator(
                tabLayout,
                fragmentViewPager
            ) { tab, position ->
                tab.text = tabNames[position]
            }.attach()

            search.searchText.addTextChangedListener {
                if (ignoreRotation) {
                    ignoreRotation = false
                    return@addTextChangedListener
                }

                childFragmentManager.setFragmentResult(
                    ARG_SEARCH_ACTION,
                    bundleOf(ARG_SEARCH_VALUE to it?.toString())
                )
            }

            if (savedInstanceState != null) {
                fragmentViewPager.currentItem = savedInstanceState.getInt(
                    SELECTED_TAB_POSITION_TAG
                )
            }
        }

        binding.channelFloatingButton.setOnClickListener{
            showAlertDialog()
        }

        return binding.root
    }

    private fun getTabs(): List<Fragment> {
        val onlySubscribed = childFragmentManager.findFragmentByTag("f0") ?: ChannelsListFragment.newInstance(
            onlySubscribed = true,
            "only subscribed"
        )

        val all = childFragmentManager.findFragmentByTag("f1") ?: ChannelsListFragment.newInstance(
            onlySubscribed = false,
            "all"
        )
        return listOf(onlySubscribed, all)
    }

    override fun onAttach(context: Context) {
        DaggerStreamComponent.factory()
            .create(context.getAppComponent())
            .inject(this)
        super.onAttach(context)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(this::binding.isInitialized)
            outState.putInt(SELECTED_TAB_POSITION_TAG, binding.layoutChannel.tabLayout.selectedTabPosition)
    }

    private fun showAlertDialog(){
        val currentBinding = LayoutCreateChannelBinding.inflate(layoutInflater)
        val alertDialogBuilderUserInput = AlertDialog.Builder(context)
        alertDialogBuilderUserInput.setView(currentBinding.root)

        alertDialogBuilderUserInput
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ok)) {
                    _, _ -> createStreamUseCase.execute(currentBinding.userInputDialog.text.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                       Toast.makeText(context, getString(R.string.create_stream_success), Toast.LENGTH_LONG).show()
                },{
                    Toast.makeText(context, getString(R.string.create_stream_unsuccess), Toast.LENGTH_LONG).show()
                })
            }
            .setNegativeButton(getString(R.string.cancel)) {
                    dialogBox, _ -> dialogBox.cancel()
            }

        val alertDialogAndroid: AlertDialog = alertDialogBuilderUserInput.create()
        alertDialogAndroid.show()
    }

    companion object {
        const val ARG_SEARCH_ACTION = "search_action"
        const val ARG_SEARCH_VALUE = "search_value"
        const val SELECTED_TAB_POSITION_TAG = "selectedTabPosition"

        fun newInstance(): ChannelsFragment {
            return ChannelsFragment()
        }
    }
}