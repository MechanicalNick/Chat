package com.tinkoff.homework.presentation.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ChannelPagerAdapter(
    fragment: Fragment,
    val tabs: List<Fragment>,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = tabs.size
    override fun createFragment(position: Int): Fragment = tabs[position]
}
