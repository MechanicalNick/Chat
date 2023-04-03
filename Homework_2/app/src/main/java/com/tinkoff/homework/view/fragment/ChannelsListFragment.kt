package com.tinkoff.homework.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.homework.App
import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.databinding.ChannelsListBinding
import com.tinkoff.homework.navigation.NavigationScreens
import com.tinkoff.homework.utils.DelegateItem
import com.tinkoff.homework.utils.Expander
import com.tinkoff.homework.utils.ToChatRouter
import com.tinkoff.homework.utils.UiState
import com.tinkoff.homework.utils.adapter.DelegatesAdapter
import com.tinkoff.homework.utils.adapter.stream.StreamDelegate
import com.tinkoff.homework.utils.adapter.stream.StreamDelegateItem
import com.tinkoff.homework.utils.adapter.topic.TopicDelegate
import com.tinkoff.homework.viewmodel.StreamViewModel
import javax.inject.Inject

class ChannelsListFragment : Fragment(), Expander, ToChatRouter {

    @Inject
    lateinit var router: Router
    lateinit var binding: ChannelsListBinding
    private lateinit var viewModel: StreamViewModel

    private val adapter: DelegatesAdapter by lazy { DelegatesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.INSTANCE.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val onlySubscribed = requireArguments().getBoolean(ARG_MESSAGE)
        val factory = StreamViewModel.Factory(onlySubscribed)
        viewModel = ViewModelProvider(this, factory)[StreamViewModel::class.java]

        viewModel.searchState.observe(viewLifecycleOwner) {
            render(it)
        }

        binding = ChannelsListBinding.inflate(layoutInflater)

        adapter.addDelegate(StreamDelegate(this))
        adapter.addDelegate(TopicDelegate(this))

        binding.channelRecyclerView.adapter = adapter

        parentFragmentManager.setFragmentResultListener(
            ChannelsFragment.ARG_SEARCH_ACTION,
            this@ChannelsListFragment
        ) { _, bundle ->
            val result = bundle.getString(ChannelsFragment.ARG_SEARCH_VALUE)
            viewModel.searchQuery(result.orEmpty())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.submitList(viewModel.factory.delegates)
        viewModel.init()
    }

    private fun render(state: UiState<List<DelegateItem>>) {
        when (state) {
            is UiState.Loading<List<DelegateItem>> -> {
                binding.shimmer.showShimmer(true)
            }
            is UiState.Data<List<DelegateItem>> -> {
                adapter.notifyDataSetChanged()
                binding.shimmer.hideShimmer()
            }
            is UiState.Error<List<DelegateItem>> -> {
                binding.shimmer.hideShimmer()
                Snackbar.make(
                    binding.root, state.exception.toString(),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun expand(item: StreamDelegateItem) {
        val index = viewModel.factory.delegates.indexOf(item)
        val stream = item.content() as Stream
        stream.isExpanded = true
        var id = index + 1L
        val newDelegates = stream.topics.map { viewModel.factory.toDelegate(it, id++) }
        viewModel.factory.delegates.addAll(index + 1, newDelegates)
        adapter.notifyItemChanged(index)
        adapter.notifyItemRangeInserted(index + 1, newDelegates.count())
    }

    override fun collapse(item: StreamDelegateItem) {
        val index = viewModel.factory.delegates.indexOf(item)
        val stream = item.content() as Stream
        stream.isExpanded = false
        var oldDelegates = mutableListOf<DelegateItem>()
        stream.topics.forEachIndexed { i, _ ->
            oldDelegates.add(viewModel.factory.delegates[index + i + 1])
        }
        viewModel.factory.delegates.removeAll(oldDelegates)
        adapter.notifyItemChanged(index)
        adapter.notifyItemRangeRemoved(index + 1, oldDelegates.count())
    }

    override fun goToChat(topicName: String, streamName: String, streamId: Long) {
        router.navigateTo(NavigationScreens.chat(topicName, streamName, streamId))
    }

    companion object {
        private const val ARG_MESSAGE = "channels"
        fun newInstance(onlySubscribed: Boolean, name: String): ChannelsListFragment {
            return ChannelsListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_MESSAGE, onlySubscribed)
                }
            }
        }
    }
}