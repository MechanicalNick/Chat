package com.tinkoff.homework.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.homework.databinding.FragmentPeopleBinding
import com.tinkoff.homework.di.component.DaggerPeoplesComponent
import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.ViewState
import com.tinkoff.homework.elm.people.model.PeopleEffect
import com.tinkoff.homework.elm.people.model.PeopleEvent
import com.tinkoff.homework.elm.people.model.PeopleState
import com.tinkoff.homework.getAppComponent
import com.tinkoff.homework.navigation.NavigationScreens
import com.tinkoff.homework.presentation.view.ToProfileRouter
import com.tinkoff.homework.presentation.view.adapter.PeopleAdapter
import com.tinkoff.homework.presentation.view.itemdecorator.MarginItemDecorator
import com.tinkoff.homework.utils.dp
import javax.inject.Inject

class PeoplesFragment : BaseFragment<PeopleEvent, PeopleEffect, PeopleState>(), ToProfileRouter {
    @Inject
    override lateinit var factory: BaseStoreFactory<PeopleEvent, PeopleEffect, PeopleState>

    @Inject
    lateinit var router: Router
    override val initEvent: PeopleEvent = PeopleEvent.Ui.LoadData

    lateinit var binding: FragmentPeopleBinding

    private val adapter: PeopleAdapter by lazy { PeopleAdapter(this) }
    private val spaceSize = 16

    override fun onAttach(context: Context) {
        DaggerPeoplesComponent.factory()
            .create(context.getAppComponent())
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeopleBinding.inflate(layoutInflater)

        binding.peopleRecyclerView.addItemDecoration(MarginItemDecorator(
            spaceSize.dp(requireContext()),
            LinearLayout.VERTICAL
        ))

        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.peopleRecyclerView.adapter = adapter

        binding.errorStateContainer.retryButton.setOnClickListener {
            this.store.accept(PeopleEvent.Ui.LoadData)
        }

        return binding.root
    }

    override fun render(state: PeopleState) {
        when (state.state) {
            ViewState.Loading -> {
                renderLoadingState(
                    shimmerFrameLayout = binding.shimmer.root,
                    errorContainer = binding.errorStateContainer.root,
                    data = binding.peopleData
                )
            }
            ViewState.Error -> {
                renderErrorState(
                    shimmerFrameLayout = binding.shimmer.root,
                    errorContainer = binding.errorStateContainer.root,
                    data = binding.peopleData
                )
                state.error?.let { throwable ->
                    binding.errorStateContainer.errorText.text = throwable.message
                }
            }
            ViewState.ShowData -> {
                renderDataState(
                    shimmerFrameLayout = binding.shimmer.root,
                    errorContainer = binding.errorStateContainer.root,
                    data = binding.peopleData
                )
                state.item?.let {
                    adapter.peoples.clear()
                    adapter.peoples.addAll(it)
                    adapter.notifyItemRangeChanged(0, it.count())
                }
            }
        }
    }

    override fun goToProfile(userId: Long) {
        this.store.accept(PeopleEvent.Ui.GoToProfile(userId))
    }

    override fun handleEffect(effect: PeopleEffect) {
        when (effect) {
            is PeopleEffect.LoadError -> Snackbar.make(
                binding.root, effect.error.toString(),
                Snackbar.LENGTH_LONG
            ).show()

            is PeopleEffect.GoToProfile -> {
                router.navigateTo(
                    NavigationScreens.profile(effect.userId)
                )
            }
        }
    }

    companion object {
        fun newInstance(): PeoplesFragment {
            return PeoplesFragment()
        }
    }
}