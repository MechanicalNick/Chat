package com.tinkoff.homework.presentation.view.fragment

import android.content.Context
import android.content.res.Configuration
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
import com.tinkoff.homework.utils.dp
import javax.inject.Inject


class ProfileFragment : BaseFragment<ProfileEvent, ProfileEffect, ProfileState>() {
    @Inject
    override lateinit var factory: BaseStoreFactory<ProfileEvent, ProfileEffect, ProfileState>

    @Inject
    lateinit var credentials: Credentials

    @Inject
    lateinit var router: Router
    override val initEvent: ProfileEvent = ProfileEvent.Ui.Wait

    lateinit var binding: FragmentProfileBinding
    private val topPortraitMargin = 113
    private val topLandscapeMargin = 15

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
        binding = FragmentProfileBinding.inflate(inflater)

        setTopMarginLayoutParams(
            topPortraitMargin, topLandscapeMargin, binding.profileImage,
            requireContext()
        )

        val userId = arguments?.getLong(ARG_MESSAGE, credentials.id) ?: run {
            credentials.id
        }
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
                    data = binding.profileData
                )
            }
            ViewState.Error -> {
                renderErrorState(
                    shimmerFrameLayout = binding.shimmer.root,
                    errorContainer = binding.errorStateContainer.root,
                    data = binding.profileData
                )
                state.error?.let { throwable ->
                    binding.errorStateContainer.errorText.text = throwable.message
                }
            }
            ViewState.ShowData -> {
                renderDataState(
                    shimmerFrameLayout = binding.shimmer.root,
                    errorContainer = binding.errorStateContainer.root,
                    data = binding.profileData
                )
                state.item?.let { renderProfile(it) }
            }
        }
    }

    override fun handleEffect(effect: ProfileEffect){
        when (effect) {
            is ProfileEffect.LoadError -> Snackbar.make(
                binding.root, effect.error.toString(),
                Snackbar.LENGTH_LONG
            ).show()
        }
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

        binding.profileToolbar.isVisible = profile.id != credentials.id

        binding.profileName.text = profile.name

        val pair = when (profile.status) {
            Status.Online -> Pair(R.string.online, R.color.green)
            Status.Idle -> Pair(R.string.idle, R.color.orange)
            Status.Offline -> Pair(R.string.offline, R.color.red)
        }

        binding.profileStatus.text = requireContext().resources.getText(pair.first)
        binding.profileStatus.setTextColor(requireContext().resources.getColor(pair.second, null))
    }


    private fun setTopMarginLayoutParams(
        topPortrait: Int,
        topLandscape: Int,
        view: View,
        context: Context
    ) {
        val orientation = resources.configuration.orientation
        val topMargin =
            if (orientation == Configuration.ORIENTATION_PORTRAIT) topPortrait.dp(context)
            else topLandscape.dp(context)
        val param = view.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, topMargin, 0, 0)
        view.layoutParams = param
    }


    companion object {
        private const val ARG_MESSAGE = "profile"
        fun newInstance(userId: Long?): ProfileFragment {
            val fragment = ProfileFragment()
            val arguments = Bundle()
            if (userId != null) {
                arguments.putLong(ARG_MESSAGE, userId)
                fragment.arguments = arguments
            }
            return fragment
        }
    }
}