package com.tinkoff.homework.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.homework.App
import com.tinkoff.homework.databinding.FragmentPeopleBinding
import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.people.model.PeopleEffect
import com.tinkoff.homework.elm.people.model.PeopleEvent
import com.tinkoff.homework.elm.people.model.PeopleState
import com.tinkoff.homework.utils.adapter.PeopleAdapter
import com.tinkoff.homework.view.itemdecorator.MarginItemDecorator
import javax.inject.Inject

class PeoplesFragment : BaseFragment<PeopleEvent, PeopleEffect, PeopleState>() {
    @Inject
    override lateinit var factory: BaseStoreFactory<PeopleEvent, PeopleEffect, PeopleState>
    override val initEvent: PeopleEvent = PeopleEvent.Ui.LoadData

    lateinit var binding: FragmentPeopleBinding

    private val adapter: PeopleAdapter by lazy { PeopleAdapter() }

    override fun onAttach(context: Context) {
        App.INSTANCE.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeopleBinding.inflate(layoutInflater)

        val space = 16f.dp(requireContext()).toInt()
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
            binding.errorStateContainer.errorText.text = throwable.message
        }
        state.item?.let {
            adapter.peoples.clear()
            adapter.peoples.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun handleEffect(effect: PeopleEffect) {
        when (effect) {
            is PeopleEffect.LoadError -> Snackbar.make(
                binding.root, effect.error.toString(),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun Float.dp(context: Context) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    )

    companion object {
        private const val ARG_MESSAGE = "people"
        fun newInstance(): PeoplesFragment {
            return PeoplesFragment()
        }
    }
}