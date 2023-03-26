package com.tinkoff.homework.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import com.tinkoff.homework.App
import com.tinkoff.homework.data.Stream
import com.tinkoff.homework.data.Topic
import com.tinkoff.homework.databinding.ChannelsListBinding
import com.tinkoff.homework.navigation.NavigationScreens
import com.tinkoff.homework.utils.DelegateItem
import com.tinkoff.homework.utils.Expander
import com.tinkoff.homework.utils.StreamFactory
import com.tinkoff.homework.utils.ToChatRouter
import com.tinkoff.homework.utils.adapter.DeleagatesAdapter
import com.tinkoff.homework.utils.adapter.stream.StreamDelegate
import com.tinkoff.homework.utils.adapter.stream.StreamDelegateItem
import com.tinkoff.homework.utils.adapter.topic.TopicDelegate
import javax.inject.Inject

class ChannelsListFragment private constructor(onlySubscribed :Boolean): Fragment(),
    Expander, ToChatRouter {
    @Inject
    lateinit var router: Router
    lateinit var binding: ChannelsListBinding

    private val adapter: DeleagatesAdapter by lazy { DeleagatesAdapter() }
    private val factory = StreamFactory(getFakeData(), onlySubscribed)

    override fun onCreate(savedInstanceState: Bundle?) {
        App.INSTANCE.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ChannelsListBinding.inflate(layoutInflater)

        adapter.addDelegate(StreamDelegate(this))
        adapter.addDelegate(TopicDelegate(this))

        adapter.submitList(factory.updateDelegateItems())

        binding.channelRecyclerView.adapter = adapter

        return binding.root
    }

    override fun expand(item: StreamDelegateItem) {
        val index = factory.delegates.indexOf(item)
        val stream = item.content() as Stream
        stream.isExpanded = true
        val newDelegates = stream.topics.map { factory.toDelegate(it) }
        factory.delegates.addAll(index+1, newDelegates)
        adapter.notifyItemChanged(index)
        adapter.notifyItemRangeInserted(index+1, newDelegates.count())
    }

    override fun collapse(item: StreamDelegateItem) {
        val index = factory.delegates.indexOf(item)
        val stream = item.content() as Stream
        stream.isExpanded = false
        var oldDelegates = mutableListOf<DelegateItem>()
        stream.topics.forEachIndexed { i, _ ->
            oldDelegates.add(factory.delegates[index + i + 1]) }
        factory.delegates.removeAll(oldDelegates)
        adapter.notifyItemChanged(index)
        adapter.notifyItemRangeRemoved(index+1, oldDelegates.count())
    }

    override fun goToChat(id: Int, charName: String) {
        router.navigateTo(NavigationScreens.chat(id, charName))
    }

    companion object {
        private const val ARG_MESSAGE = "channels"
        fun newInstance(onlySubscribed :Boolean, name: String): ChannelsListFragment {
            return ChannelsListFragment(onlySubscribed).apply {
                arguments = Bundle().apply {
                    putString(ARG_MESSAGE, name)
                }
            }
        }

        fun getFakeData(): MutableList<Stream>{
            val topics1 = listOf(
                Topic(1, "Topic1", 100),
                Topic(2, "Topic2", 500),
                Topic(3, "Topic3", 300),
                Topic(4, "Topic4", 300),
                Topic(5, "Topic5", 300),
                Topic(6, "Topic6", 300),
                Topic(7, "Topic7", 300),
                Topic(8, "Topic9", 300),
                Topic(9, "Topic9", 300),
            )

            val topics3 = listOf(
                Topic(4, "Topic4", 12345)
            )

            var streams = mutableListOf(
                Stream(1, "Stream1", topics1, isSubscribed = true, isExpanded = false),
                Stream(2, "Stream2", emptyList(), isSubscribed = false, isExpanded = false),
                Stream(3, "Stream3", topics3, isSubscribed = false, isExpanded = false)
            )

            return streams
        }
    }
}