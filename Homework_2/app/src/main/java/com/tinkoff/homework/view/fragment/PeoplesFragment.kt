package com.tinkoff.homework.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.homework.data.domain.People
import com.tinkoff.homework.databinding.FragmentPeopleBinding
import com.tinkoff.homework.utils.UiState
import com.tinkoff.homework.utils.adapter.PeopleAdapter
import com.tinkoff.homework.view.itemdecorator.MarginItemDecorator
import com.tinkoff.homework.viewmodel.PeoplesViewModel

class PeoplesFragment: Fragment() {
    lateinit var binding: FragmentPeopleBinding

    private val viewModel: PeoplesViewModel by viewModels()
    private val adapter: PeopleAdapter by lazy { PeopleAdapter() }

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
        binding.peopleRecyclerView.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.init()

        return binding.root
    }

    private fun render(state: UiState<List<People>>?) {
        when (state) {
            is UiState.Loading<List<People>> -> {
                binding.shimmer.showShimmer(true)
            }
            is UiState.Data<List<People>> -> {
                adapter.peoples.clear()
                adapter.peoples.addAll(state.data)
                adapter.notifyDataSetChanged()
                binding.shimmer.hideShimmer()
            }
            is UiState.Error<List<People>> -> {
                binding.shimmer.hideShimmer()
                Snackbar.make(
                    binding.root, state.exception.toString(),
                    Snackbar.LENGTH_LONG
                ).show()
            }
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