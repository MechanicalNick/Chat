package com.tinkoff.homework.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
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
import com.tinkoff.homework.presentation.dp
import com.tinkoff.homework.presentation.view.ToProfileRouter
import com.tinkoff.homework.presentation.view.adapter.DelegatesAdapter
import com.tinkoff.homework.presentation.view.adapter.people.PeopleDelegate
import com.tinkoff.homework.presentation.view.adapter.people.PeopleDelegateItem
import com.tinkoff.homework.presentation.view.itemdecorator.MarginItemDecorator
import com.tinkoff.homework.presentation.viewmodel.ChannelsViewModel
import com.tinkoff.homework.presentation.viewmodel.PeopleViewModel
import javax.inject.Inject

class PeoplesFragment : BaseFragment<PeopleEvent, PeopleEffect, PeopleState>(), ToProfileRouter {

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!
    private val adapter: DelegatesAdapter by lazy { DelegatesAdapter() }
    private val spaceSize = 16

    override val initEvent: PeopleEvent = PeopleEvent.Ui.LoadData

    @Inject
    override lateinit var factory: BaseStoreFactory<PeopleEvent, PeopleEffect, PeopleState>

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var peopleViewModel: PeopleViewModel

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
        _binding = FragmentPeopleBinding.inflate(layoutInflater)

        binding.peopleRecyclerView.addItemDecoration(MarginItemDecorator(
            spaceSize.dp(requireContext()),
            LinearLayout.VERTICAL
        ))

        adapter.addDelegate(PeopleDelegate(this))
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.peopleRecyclerView.adapter = adapter

        binding.errorStateContainer.retryButton.setOnClickListener {
            this.store.accept(PeopleEvent.Ui.LoadData)
        }

        peopleViewModel.store = this.store

        binding.peopleSearch.searchText.addTextChangedListener{
            val query = it?.toString().orEmpty()
            peopleViewModel.searchQueryPublisher.onNext(query)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun render(state: PeopleState) {
        when (state.state) {
            ViewState.Loading -> {
                renderLoadingState(
                    shimmerFrameLayout = binding.shimmer.root,
                    errorContainer = binding.errorStateContainer.root,
                    data = binding.peopleRecyclerView
                )
            }
            ViewState.Error -> {
                renderErrorState(
                    shimmerFrameLayout = binding.shimmer.root,
                    errorContainer = binding.errorStateContainer.root,
                    data = binding.peopleRecyclerView
                )
                state.error?.let { throwable ->
                    binding.errorStateContainer.errorText.text = throwable.message
                }
            }
            ViewState.ShowData -> {
                renderDataState(
                    shimmerFrameLayout = binding.shimmer.root,
                    errorContainer = binding.errorStateContainer.root,
                    data = binding.peopleRecyclerView
                )
                state.item?.let { peoples ->
                    adapter.submitList(peoples.map { people ->
                        PeopleDelegateItem(people.userId, people)
                    })
                }
            }
        }
        binding.peopleProgressBar.isVisible = state.isShowProgress
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
                    NavigationScreens.profile(userId = effect.userId, fromMyProfile = false)
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