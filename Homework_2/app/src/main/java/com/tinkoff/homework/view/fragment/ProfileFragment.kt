package com.tinkoff.homework.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.homework.R
import com.tinkoff.homework.data.domain.Profile
import com.tinkoff.homework.data.domain.Status
import com.tinkoff.homework.databinding.FragmentProfileBinding
import com.tinkoff.homework.utils.UiState
import com.tinkoff.homework.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var  viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileId = arguments?.getLong(ARG_MESSAGE) ?: null
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
        binding.profileImage.let {
            Glide.with(binding.root)
                .load(profile.avatarUrl)
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.drawable.error_placeholder)
                .into(it)
        }


        binding.profileName.text = profile.name

        val pair = when(profile.status){
            Status.Online -> Pair(R.string.online, R.color.green)
            Status.Idle -> Pair(R.string.idle, R.color.orange)
            Status.Offline ->  Pair(R.string.offline, R.color.red)
        }

        binding.status.text = requireContext().resources.getText(pair.first)
        binding.status.setTextColor(requireContext().resources.getColor(pair.second, null))
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
        fun newInstance(userId: Long?): ProfileFragment {
            var fragment = ProfileFragment()
            val arguments = Bundle()
            if(userId !=null) {
                arguments.putLong(ARG_MESSAGE, userId)
                fragment.arguments = arguments
            }
            return fragment
        }
    }
}