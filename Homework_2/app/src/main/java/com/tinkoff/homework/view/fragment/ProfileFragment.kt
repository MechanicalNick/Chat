package com.tinkoff.homework.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.tinkoff.homework.R
import com.tinkoff.homework.data.Profile
import com.tinkoff.homework.data.Status
import com.tinkoff.homework.databinding.FragmentProfileBinding
import com.tinkoff.homework.utils.Const

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileId = requireArguments().getInt(ARG_MESSAGE)
        val profile = getProfile(profileId)
        
        binding = FragmentProfileBinding.inflate(inflater)

        val image = ResourcesCompat.getDrawable(requireContext().resources,
            R.drawable.avatar, null)
        binding.profileImage.setImageDrawable(image)

        binding.profileDescription.text = profile.description
        binding.profileName.text = profile.name

        var triple = if(profile.status == Status.Online) {
            Triple(R.string.online, R.color.green, View.VISIBLE)
        } else {
            Triple(R.string.offline, R.color.red, View.GONE)
        }

        binding.status.text = requireContext().resources.getText(triple.first)
        binding.status.setTextColor(requireContext().resources.getColor(triple.second, null))
        binding.profileLogout.visibility = triple.third

        return binding.root
    }

    companion object {
        private const val ARG_MESSAGE = "profile"
        fun newInstance(userId: Int): ProfileFragment {
            var fragment = ProfileFragment()
            val arguments = Bundle()
            arguments.putInt(ARG_MESSAGE, userId)
            fragment.arguments = arguments
            return fragment
        }

        private fun getProfile(id : Int):Profile{
            return Profile(Const.myId, "NAME SURNAME", "description",
                Status.Online)
        }
    }
}