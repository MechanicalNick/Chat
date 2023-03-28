package com.tinkoff.homework.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.homework.R
import com.tinkoff.homework.data.domain.Profile
import com.tinkoff.homework.data.Status
import com.tinkoff.homework.data.domain.People
import com.tinkoff.homework.databinding.FragmentProfileBinding
import com.tinkoff.homework.utils.Const
import com.tinkoff.homework.utils.UiState
import com.tinkoff.homework.viewmodel.PeoplesViewModel
import com.tinkoff.homework.viewmodel.ProfileViewModel
import com.tinkoff.homework.viewmodel.StreamViewModel

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var  viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileId = requireArguments().getInt(ARG_MESSAGE)
        val factory = ProfileViewModel.Factory(profileId)
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        binding = FragmentProfileBinding.inflate(inflater)

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.init()

        return binding.root
    }

    private fun renderProfile(profile: Profile) {
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
    }

    private fun render(state: UiState<Profile>) {
        when (state) {
            is UiState.Loading<Profile> -> {
                binding.shimmer.showShimmer(true)
            }
            is UiState.Data<Profile> -> {
                renderProfile(state.data)
                binding.shimmer.hideShimmer()
            }
            is UiState.Error<Profile> -> {
                binding.shimmer.hideShimmer()
                Snackbar.make(
                    binding.root, state.exception.toString(),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
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
    }
}