package com.tinkoff.homework.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.tinkoff.homework.presentation.view.ToProfileRouter
import com.tinkoff.homework.presentation.view.adapter.DelegatesAdapter
import com.tinkoff.homework.presentation.view.adapter.people.PeopleDelegate
import com.tinkoff.homework.presentation.view.adapter.people.PeopleDelegateItem
import com.tinkoff.homework.presentation.view.itemdecorator.MarginItemDecorator
import com.tinkoff.homework.utils.dp
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PeoplesFragment : BaseFragment<PeopleEvent, PeopleEffect, PeopleState>(), ToProfileRouter {
    @Inject
    override lateinit var factory: BaseStoreFactory<PeopleEvent, PeopleEffect, PeopleState>

    @Inject
    lateinit var router: Router
    override val initEvent: PeopleEvent = PeopleEvent.Ui.LoadData

    lateinit var binding: FragmentPeopleBinding

    private val searchQueryPublisher: PublishSubject<String> = PublishSubject.create()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val adapter: DelegatesAdapter by lazy { DelegatesAdapter() }
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

        adapter.addDelegate(PeopleDelegate(this))
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.peopleRecyclerView.adapter = adapter

        binding.errorStateContainer.retryButton.setOnClickListener {
            this.store.accept(PeopleEvent.Ui.LoadData)
        }

        searchQueryPublisher
            .distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .subscribeBy { searchQuery ->
                Log.e("QUERY", searchQuery)
                this.store.accept(PeopleEvent.Ui.Search(searchQuery))
            }
            .addTo(compositeDisposable)

        binding.peopleSearch.searchText.addTextChangedListener{
            searchQueryPublisher.onNext(it.toString())
        }

        return binding.root
    }
    
    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
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