package com.tinkoff.homework.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.databinding.ChannelsListBinding
import com.tinkoff.homework.di.component.DaggerStreamComponent
import com.tinkoff.homework.elm.channels.ChannelsStoreFactory
import com.tinkoff.homework.elm.channels.model.ChannelsEffect
import com.tinkoff.homework.elm.channels.model.ChannelsEvent
import com.tinkoff.homework.elm.channels.model.ChannelsState
import com.tinkoff.homework.getAppComponent
import com.tinkoff.homework.navigation.NavigationScreens
import com.tinkoff.homework.utils.Expander
import com.tinkoff.homework.utils.StreamFactory
import com.tinkoff.homework.utils.ToChatRouter
import com.tinkoff.homework.utils.adapter.DelegatesAdapter
import com.tinkoff.homework.utils.adapter.stream.StreamDelegate
import com.tinkoff.homework.utils.adapter.stream.StreamDelegateItem
import com.tinkoff.homework.utils.adapter.topic.TopicDelegate
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChannelsListFragment : ElmFragment<ChannelsEvent, ChannelsEffect, ChannelsState>(), Expander,
    ToChatRouter {
    @Inject
    lateinit var channelsStoreFactory: ChannelsStoreFactory
    @Inject
    lateinit var streamFactories: Map<Boolean, StreamFactory>
    lateinit var streamFactory: StreamFactory
    @Inject
    lateinit var router: Router
    private val searchQueryPublisher: PublishSubject<String> = PublishSubject.create()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override val initEvent: ChannelsEvent = ChannelsEvent.Ui.Wait
    lateinit var binding: ChannelsListBinding
    private var query = ""
    private val adapter: DelegatesAdapter by lazy { DelegatesAdapter() }

    override fun onAttach(context: Context) {
        DaggerStreamComponent.factory()
            .create(context.getAppComponent())
            .inject(this)
        val onlySubscribed = requireArguments().getBoolean(ARG_MESSAGE)
        streamFactory = streamFactories[onlySubscribed]!!
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
        binding = ChannelsListBinding.inflate(layoutInflater)

        adapter.addDelegate(StreamDelegate(this))
        adapter.addDelegate(TopicDelegate(this))
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.channelRecyclerView.adapter = adapter

        parentFragmentManager.setFragmentResultListener(
            ChannelsFragment.ARG_SEARCH_ACTION,
            this@ChannelsListFragment
        ) { _, bundle ->
            val result = bundle.getString(ChannelsFragment.ARG_SEARCH_VALUE)
            query = result.orEmpty()
            searchQueryPublisher.onNext(query)
        }

        val savedState = savedStateRegistry
            .consumeRestoredStateForKey(ARG_STATE)?.
            getParcelable<ChannelsState>(ARG_STATE)

        savedState?.items?.let {
            this.store.accept(ChannelsEvent.Internal.DataLoaded(it))
        }?: run {
            this.store.accept(ChannelsEvent.Ui.LoadData)
        }

        searchQueryPublisher
            .distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .subscribeBy { searchQuery ->
                this.store.accept(ChannelsEvent.Ui.Search(searchQuery))
            }
            .addTo(compositeDisposable)


        searchQueryPublisher.onNext(query)

        return binding.root
    }

    override val storeHolder: StoreHolder<ChannelsEvent, ChannelsEffect, ChannelsState> by lazy {
        val onlySubscribed = requireArguments().getBoolean(ARG_MESSAGE)
        val store = channelsStoreFactory.provide(onlySubscribed)
        store.stop()
        LifecycleAwareStoreHolder(lifecycle) {
            store
        }
    }

    override fun render(state: ChannelsState) {
        if (state.isLoading)
            binding.shimmer.showShimmer(true)
        else
            binding.shimmer.hideShimmer()

        state.items?.let {
            streamFactory.updateDelegateItems(state.items)
            adapter.submitList(streamFactory.delegates)
            adapter.notifyDataSetChanged()
        }
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
        compositeDisposable.dispose()
        super.onDestroy()
    }

    companion object {
        private const val ARG_MESSAGE = "channels"
        private const val ARG_STATE = "CHANNEL_LIST_STATE"
        private const val ARG_QUERY = "QUERY"
        fun newInstance(onlySubscribed: Boolean, name: String): ChannelsListFragment {
            return ChannelsListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_MESSAGE, onlySubscribed)
                }
            }
        }
    }
}