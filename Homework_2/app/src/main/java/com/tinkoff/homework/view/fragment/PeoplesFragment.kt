package com.tinkoff.homework.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.homework.databinding.FragmentPeopleBinding
import com.tinkoff.homework.di.component.DaggerPeoplesComponent
import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.people.model.PeopleEffect
import com.tinkoff.homework.elm.people.model.PeopleEvent
import com.tinkoff.homework.elm.people.model.PeopleState
import com.tinkoff.homework.getAppComponent
import com.tinkoff.homework.navigation.NavigationScreens
import com.tinkoff.homework.utils.ToProfileRouter
import com.tinkoff.homework.utils.adapter.PeopleAdapter
import com.tinkoff.homework.utils.dp
import com.tinkoff.homework.view.itemdecorator.MarginItemDecorator
import javax.inject.Inject

class PeoplesFragment : BaseFragment<PeopleEvent, PeopleEffect, PeopleState>(), ToProfileRouter {
    @Inject
    override lateinit var factory: BaseStoreFactory<PeopleEvent, PeopleEffect, PeopleState>

    @Inject
    lateinit var router: Router
    override val initEvent: PeopleEvent = PeopleEvent.Ui.LoadData

    lateinit var binding: FragmentPeopleBinding

    private val adapter: PeopleAdapter by lazy { PeopleAdapter(this) }

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

        val space = 16.dp(requireContext())
        val itemDecoration = MarginItemDecorator(
            space,
            LinearLayout.VERTICAL
        )

        binding.peopleRecyclerView.addItemDecoration(itemDecoration)
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.peopleRecyclerView.adapter = adapter

        return binding.root
    }

    override fun render(state: PeopleState) {
        if (state.isLoading)
            binding.shimmer.showShimmer(true)
        else
            binding.shimmer.hideShimmer()
        state.error?.let { throwable ->
            binding.errorStateContainer.errorLayout.isVisible = true
            binding.errorStateContainer.errorText.text = throwable.message
        } ?: run {
            binding.errorStateContainer.errorLayout.isVisible = false
            state.item?.let {
                adapter.peoples.clear()
                adapter.peoples.addAll(it)
                adapter.notifyDataSetChanged()
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