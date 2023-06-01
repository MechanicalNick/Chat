package com.tinkoff.homework.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.homework.R
import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.databinding.FragmentProfileBinding
import com.tinkoff.homework.di.component.DaggerProfileComponent
import com.tinkoff.homework.domain.data.Profile
import com.tinkoff.homework.domain.data.Status
import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.ViewState
import com.tinkoff.homework.elm.profile.model.ProfileEffect
import com.tinkoff.homework.elm.profile.model.ProfileEvent
import com.tinkoff.homework.elm.profile.model.ProfileState
import com.tinkoff.homework.getAppComponent
import com.tinkoff.homework.presentation.dp
import javax.inject.Inject


class ProfileFragment : BaseFragment<ProfileEvent, ProfileEffect, ProfileState>() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var fromMyProfile = true

    override val initEvent: ProfileEvent = ProfileEvent.Ui.Wait

    @Inject
    override lateinit var factory: BaseStoreFactory<ProfileEvent, ProfileEffect, ProfileState>

    @Inject
    lateinit var credentials: Credentials

    @Inject
    lateinit var router: Router

    override fun onAttach(context: Context) {
        DaggerProfileComponent.factory()
            .create(context.getAppComponent())
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)

        val userId = arguments?.getLong(ARG_PROFILE_ID, credentials.id) ?: run {
            credentials.id
        }
        fromMyProfile = arguments?.getBoolean(ARG_FROM_MY_PROFILE)?: run { true }

        this.store.accept(ProfileEvent.Ui.LoadData(userId))

        binding.errorStateContainer.retryButton.setOnClickListener {
            this.store.accept(ProfileEvent.Ui.LoadData(userId))
        }
        binding.profileToolbar.setNavigationOnClickListener {
            router.exit()
        }

        return binding.root
    }

    override fun render(state: ProfileState) {
        when (state.state) {
            ViewState.Loading -> {
                renderLoadingState(
                    shimmerFrameLayout = binding.shimmer.root,
                    errorContainer = binding.errorStateContainer.root,
                    data = binding.profileData as ViewGroup
                )
            }
            ViewState.Error -> {
                renderErrorState(
                    shimmerFrameLayout = binding.shimmer.root,
                    errorContainer = binding.errorStateContainer.root,
                    data = binding.profileData as ViewGroup
                )
                state.error?.let { throwable ->
                    binding.errorStateContainer.errorText.text = throwable.message
                }
            }
            ViewState.ShowData -> {
                renderDataState(
                    shimmerFrameLayout = binding.shimmer.root,
                    errorContainer = binding.errorStateContainer.root,
                    data = binding.profileData as ViewGroup
                )
                state.item?.let { renderProfile(it) }
            }
        }
    }

    override fun handleEffect(effect: ProfileEffect) {
        when (effect) {
            is ProfileEffect.LoadError -> Snackbar.make(
                binding.root, effect.error.toString(),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderProfile(profile: Profile) {
        binding.profileImage.let {
            Glide.with(binding.root)
                .load(profile.avatarUrl)
                .transform(RoundedCorners(15.dp(binding.root.context)))
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.drawable.error_placeholder)
                .into(it)
        }

        binding.profileToolbar.isVisible = !fromMyProfile

        binding.profileName.text = profile.name

        val pair = when (profile.status) {
            Status.Online -> Pair(R.string.online, R.color.green)
            Status.Idle -> Pair(R.string.idle, R.color.orange)
            Status.Offline -> Pair(R.string.offline, R.color.red)
        }

        binding.profileStatus.text = requireContext().resources.getText(pair.first)
        binding.profileStatus.setTextColor(requireContext().resources.getColor(pair.second, null))
    }

    companion object {
        private const val ARG_PROFILE_ID = "profile"
        private const val ARG_FROM_MY_PROFILE = "from my profile"
        fun newInstance(userId: Long?, fromMyProfile: Boolean): ProfileFragment {
            val fragment = ProfileFragment()
            val arguments = Bundle()
            if (userId != null) {
                arguments.putLong(ARG_PROFILE_ID, userId)
                fragment.arguments = arguments
            }
            arguments.putBoolean(ARG_FROM_MY_PROFILE, fromMyProfile)
            return fragment
        }
    }
}