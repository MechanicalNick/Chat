package com.tinkoff.homework.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.FragmentChannelBinding
import com.tinkoff.homework.utils.adapter.ChannelPagerAdapter

class ChannelsFragment: Fragment()  {
    private lateinit var binding: FragmentChannelBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChannelBinding.inflate(layoutInflater)

        val tabs: List<String> = listOf(getString(R.string.subscribed),
            getString(R.string.all_stream))
        val pagerAdapter = ChannelPagerAdapter(childFragmentManager, lifecycle)

        binding.fragmentViewPager.adapter = pagerAdapter

        val onlySubscribed =
            ChannelsListFragment.newInstance(onlySubscribed = true, "onlySubscribed")
        val all = ChannelsListFragment.newInstance(onlySubscribed = false, "all")
        pagerAdapter.update(listOf(onlySubscribed, all))

        TabLayoutMediator(binding.tabLayout, binding.fragmentViewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        return binding.root
    }

    companion object {
        private const val ARG_MESSAGE = "channels"
        fun newInstance(): ChannelsFragment {
            return ChannelsFragment()
        }
    }
}