package com.tinkoff.homework.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.homework.App
import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.databinding.ChannelsListBinding
import com.tinkoff.homework.elm.channels.ChannelsStoreFactory
import com.tinkoff.homework.elm.channels.model.ChannelsEffect
import com.tinkoff.homework.elm.channels.model.ChannelsEvent
import com.tinkoff.homework.elm.channels.model.ChannelsState
import com.tinkoff.homework.navigation.NavigationScreens
import com.tinkoff.homework.utils.Expander
import com.tinkoff.homework.utils.StreamFactory
import com.tinkoff.homework.utils.ToChatRouter
import com.tinkoff.homework.utils.adapter.DelegatesAdapter
import com.tinkoff.homework.utils.adapter.stream.StreamDelegate
import com.tinkoff.homework.utils.adapter.stream.StreamDelegateItem
import com.tinkoff.homework.utils.adapter.topic.TopicDelegate
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

class ChannelsListFragment : ElmFragment<ChannelsEvent, ChannelsEffect, ChannelsState>(), Expander,
    ToChatRouter {
    @Inject
    lateinit var factory: ChannelsStoreFactory

    @Inject
    lateinit var router: Router
    override val initEvent: ChannelsEvent = ChannelsEvent.Ui.LoadData
    lateinit var binding: ChannelsListBinding

    private val streamFactory = StreamFactory()
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
            this.store.accept(ChannelsEvent.Ui.Search(result.orEmpty()))
        }

        return binding.root
    }

    override val storeHolder: StoreHolder<ChannelsEvent, ChannelsEffect, ChannelsState> by lazy {
        val onlySubscribed = requireArguments().getBoolean(ARG_MESSAGE)
        val store = factory.provide(onlySubscribed)
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
        this.store.accept(ChannelsEvent.Ui.ExpandStream(stream))
    }

    override fun collapse(item: StreamDelegateItem) {
        val stream = item.content() as Stream
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