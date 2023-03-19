package com.tinkoff.homework.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tinkoff.homework.R

class ChannelsFragment : Fragment()  {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_channel, container, false)
    }

    companion object {
        private const val ARG_MESSAGE = "channels"
        fun newInstance(): ChannelsFragment {
            return ChannelsFragment()
        }
    }
}