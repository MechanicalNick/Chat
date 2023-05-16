package com.tinkoff.homework.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.ChannelsListBinding
import com.tinkoff.homework.di.component.DaggerStreamComponent
import com.tinkoff.homework.domain.data.Stream
import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.ViewState
import com.tinkoff.homework.elm.channels.model.ChannelsEffect
import com.tinkoff.homework.elm.channels.model.ChannelsEvent
import com.tinkoff.homework.elm.channels.model.ChannelsState
import com.tinkoff.homework.getAppComponent
import com.tinkoff.homework.navigation.NavigationScreens
import com.tinkoff.homework.presentation.view.StreamFactory
import com.tinkoff.homework.presentation.view.adapter.DelegatesAdapter
import com.tinkoff.homework.presentation.view.adapter.stream.StreamDelegate
import com.tinkoff.homework.presentation.view.adapter.stream.StreamDelegateItem
import com.tinkoff.homework.presentation.view.adapter.topic.TopicDelegate
import com.tinkoff.homework.presentation.view.viewgroup.StreamView
import com.tinkoff.homework.presentation.viewmodel.ChannelsViewModel
import javax.inject.Inject


class ChannelsListFragment : BaseFragment<ChannelsEvent, ChannelsEffect, ChannelsState>(),
    StreamView {

    private var _binding: ChannelsListBinding? = null
    private val binding get() = _binding!!
    private val adapter: DelegatesAdapter by lazy { DelegatesAdapter() }
    private lateinit var streamFactory: StreamFactory

    override lateinit var factory: BaseStoreFactory<ChannelsEvent, ChannelsEffect, ChannelsState>
    override val initEvent: ChannelsEvent = ChannelsEvent.Ui.Wait

    // https://stackoverflow.com/questions/43141740/dagger-2-multibindings-with-kotlin/43149382#43149382
    @Inject
    lateinit var channelsStoreFactories: Map<Boolean,
            @JvmSuppressWildcards BaseStoreFactory<ChannelsEvent, ChannelsEffect, ChannelsState>>

    @Inject
    lateinit var streamFactories: Map<Boolean, StreamFactory>

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var channelViewModel: ChannelsViewModel

    override fun onAttach(context: Context) {
        DaggerStreamComponent.factory()
            .create(context.getAppComponent())
            .inject(this)
        val onlySubscribed = requireArguments().getBoolean(ARG_MESSAGE)
        streamFactory = streamFactories[onlySubscribed]!!
        factory = channelsStoreFactories[onlySubscribed]!!
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        savedStateRegistry.registerSavedStateProvider(ARG_STATE) {
            Bundle().apply { putParcelable(ARG_STATE, (store.currentState)) }
        }

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChannelsListBinding.inflate(layoutInflater)
        val dividerItemDecoration = DividerItemDecoration(
            binding.channelRecyclerView.context,
            DividerItemDecoration.VERTICAL
        )
        context?.let { ctx ->
            ResourcesCompat.getDrawable(ctx.resources, R.drawable.item_decorator, ctx.theme)?.let {
                drawable -> dividerItemDecoration.setDrawable(drawable)
            }
        }

        binding.channelRecyclerView.addItemDecoration(dividerItemDecoration)
        adapter.addDelegate(StreamDelegate(this))
        adapter.addDelegate(TopicDelegate(this))
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.channelRecyclerView.adapter = adapter

        channelViewModel.store = store

        parentFragmentManager.setFragmentResultListener(
            ChannelsFragment.ARG_SEARCH_ACTION,
            this@ChannelsListFragment
        ) { _, bundle ->
            val query = bundle.getString(ChannelsFragment.ARG_SEARCH_VALUE).orEmpty()
            channelViewModel.searchQueryPublisher.onNext(query)
        }

        val savedState = savedStateRegistry
            .consumeRestoredStateForKey(ARG_STATE)?.
            getParcelable<ChannelsState>(ARG_STATE)

        savedState?.items?.let {
            this.store.accept(ChannelsEvent.Internal.DataLoaded(it))
        }?: run {
            loadData()
        }

        binding.errorStateContainer.retryButton.setOnClickListener {
            loadData()
        }

        return binding.root
    }

    override fun render(state: ChannelsState) {
        when(state.state){
            ViewState.Loading -> {
                renderLoadingState(
                    binding.shimmer.root,
                    binding.errorStateContainer.root,
                    binding.channelRecyclerView
                )
            }
            ViewState.Error -> {
                renderErrorState(
                    binding.shimmer.root,
                    binding.errorStateContainer.root,
                    binding.channelRecyclerView
                )
                state.error?.let { throwable ->
                    binding.errorStateContainer.errorText.text = throwable.message
                }
            }
            ViewState.ShowData -> {
                renderDataState(
                    binding.shimmer.root,
                    binding.errorStateContainer.root,
                    binding.channelRecyclerView
                )
              state.items?.let {
                  val delegates = streamFactory.updateDelegateItems(it)
                  adapter.submitList(delegates)
                  binding.channelRecyclerView.isVisible = state.items.isNotEmpty()
                  binding.emptyLayout.root.isVisible = state.items.isEmpty()
              }
            }
        }
        binding.progressBar.isVisible = state.isShowProgress
    }

    override fun expand(item: StreamDelegateItem) {
        val stream = item.content() as Stream
        streamFactory.updateState(stream.id, true)
        this.store.accept(ChannelsEvent.Ui.ExpandStream(stream))
    }

    override fun collapse(item: StreamDelegateItem) {
        val stream = item.content() as Stream
        streamFactory.updateState(stream.id, false)
        this.store.accept(ChannelsEvent.Ui.CollapseStream(stream))
    }

    override fun handleEffect(effect: ChannelsEffect): Unit =
        when (effect) {
            is ChannelsEffect.GoToChat -> {
                router.navigateTo(
                    NavigationScreens.chat(
                        effect.topicName,
                        effect.streamName, effect.streamId
                    )
                )
            }
            is ChannelsEffect.LoadError -> Snackbar.make(
                binding.root, effect.error.toString(),
                Snackbar.LENGTH_LONG
            ).show()
        }

    override fun goToChat(topicName: String, streamName: String, streamId: Long) {
        this.store.accept(ChannelsEvent.Ui.GoToChat(topicName, streamName, streamId))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun loadData() {
        this.store.accept(ChannelsEvent.Ui.LoadData)
    }

    companion object {
        private const val ARG_MESSAGE = "channels"
        private const val ARG_STATE = "CHANNEL_LIST_STATE"
        fun newInstance(onlySubscribed: Boolean): ChannelsListFragment {
            return ChannelsListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_MESSAGE, onlySubscribed)
                }
            }
        }
    }
}