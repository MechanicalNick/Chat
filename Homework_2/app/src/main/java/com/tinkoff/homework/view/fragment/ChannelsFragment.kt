package com.tinkoff.homework.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.FragmentChannelBinding
import com.tinkoff.homework.utils.adapter.ChannelPagerAdapter

class ChannelsFragment: Fragment()  {
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

        binding.fragmentViewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.fragmentViewPager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

        binding.search.addTextChangedListener {
            if(ignoreRotation) {
                ignoreRotation = false
                return@addTextChangedListener
            }

            childFragmentManager.setFragmentResult(
                ARG_SEARCH_ACTION,
                bundleOf(ARG_SEARCH_VALUE to it?.toString())
            )
        }

        if(savedInstanceState != null ){
            binding.fragmentViewPager.currentItem = savedInstanceState.getInt(
                SELECTED_TAB_POSITION_TAG)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(this::binding.isInitialized)
            outState.putInt(SELECTED_TAB_POSITION_TAG, binding.tabLayout.selectedTabPosition)
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