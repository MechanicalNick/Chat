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
import com.tinkoff.homework.navigation.Expander
import com.tinkoff.homework.navigation.NavigationScreens
import com.tinkoff.homework.navigation.StreamFactory
import com.tinkoff.homework.navigation.ToChatRouter
import com.tinkoff.homework.presentation.DelegatesAdapter
import com.tinkoff.homework.presentation.view.adapter.stream.StreamDelegate
import com.tinkoff.homework.presentation.view.adapter.stream.StreamDelegateItem
import com.tinkoff.homework.presentation.view.adapter.topic.TopicDelegate
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ChannelsListFragment : BaseFragment<ChannelsEvent, ChannelsEffect, ChannelsState>(), Expander,
    ToChatRouter {
    // https://stackoverflow.com/questions/43141740/dagger-2-multibindings-with-kotlin/43149382#43149382
    @Inject
    lateinit var channelsStoreFactories: Map<Boolean,
            @JvmSuppressWildcards BaseStoreFactory<ChannelsEvent, ChannelsEffect, ChannelsState>>
    @Inject
    lateinit var streamFactories: Map<Boolean, StreamFactory>
    @Inject
    lateinit var router: Router
    override lateinit var factory: BaseStoreFactory<ChannelsEvent, ChannelsEffect, ChannelsState>
    override val initEvent: ChannelsEvent = ChannelsEvent.Ui.Wait
    lateinit var binding: ChannelsListBinding
    private lateinit var streamFactory: StreamFactory
    private val searchQueryPublisher: PublishSubject<String> = PublishSubject.create()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var query = ""
    private val adapter: DelegatesAdapter by lazy { DelegatesAdapter() }

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
        binding = ChannelsListBinding.inflate(layoutInflater)
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
            loadData()
        }

        binding.errorStateContainer.retryButton.setOnClickListener(){
            loadData()
        }

        searchQueryPublisher
            .distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .subscribeBy { searchQuery ->
                this.store.accept(ChannelsEvent.Ui.Search(searchQuery))
            }
            .addTo(compositeDisposable)

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
              }
            }
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

    private fun loadData() {
        this.store.accept(ChannelsEvent.Ui.LoadCashedData)
        this.store.accept(ChannelsEvent.Ui.LoadData)
    }

    companion object {
        private const val ARG_MESSAGE = "channels"
        private const val ARG_STATE = "CHANNEL_LIST_STATE"
        fun newInstance(onlySubscribed: Boolean, name: String): ChannelsListFragment {
            return ChannelsListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_MESSAGE, onlySubscribed)
                }
            }
        }
    }
}