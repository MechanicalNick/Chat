package com.tinkoff.homework.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.tinkoff.homework.data.People
import com.tinkoff.homework.data.Status
import com.tinkoff.homework.databinding.FragmentPeopleBinding
import com.tinkoff.homework.utils.adapter.PeopleAdapter
import com.tinkoff.homework.view.itemdecorator.MarginItemDecorator

class PeoplesFragment private constructor(): Fragment() {
    lateinit var binding: FragmentPeopleBinding

    private val adapter: PeopleAdapter by lazy { PeopleAdapter(peoples) }

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
        return binding.root
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

        val peoples = IntRange(1, 15)
            .map { People("Name${it}", "email${it}@gmail.com)",
                if(it % 2 ==0) Status.Online else Status.Offline)
            }
    }
}