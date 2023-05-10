package com.tinkoff.homework.presentation.view.adapter.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.TopicItemBinding
import com.tinkoff.homework.domain.data.Topic
import com.tinkoff.homework.navigation.DelegateItem
import com.tinkoff.homework.navigation.ToChatRouter
import com.tinkoff.homework.presentation.AdapterDelegate

class TopicDelegate(private val router: ToChatRouter) : AdapterDelegate {

    private val backgrounds = listOf(R.color.background_topic_color_1,
        R.color.background_topic_color_2,
        R.color.background_topic_color_3,
        R.color.background_topic_color_4,
        R.color.background_topic_color_5,
        R.color.background_topic_color_6,
        R.color.background_topic_color_7)

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            TopicItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            backgrounds,
            router
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(item.content() as Topic, position)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is TopicDelegateItem

    class ViewHolder(
        private val binding: TopicItemBinding,
        private val backgrounds: List<Int>,
        private val router: ToChatRouter
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Topic, position: Int) {
            with(binding) {
                topicName.text = model.name
                val messages = binding.root.context.resources.getString(R.string.messages)
                messageCount.text = "${model.messageCount} $messages"
                val color = backgrounds[position % backgrounds.size]
                topicItemLayout.background = ResourcesCompat
                    .getDrawable(binding.root.context.resources, color, null)
                binding.root.setOnClickListener{
                    router.goToChat(model.name, model.streamName, model.streamId)
                }
            }
        }
    }
}