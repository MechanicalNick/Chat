package com.tinkoff.homework.presentation.view.adapter.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.TopicMessageItemBinding
import com.tinkoff.homework.domain.data.TopicModel
import com.tinkoff.homework.presentation.view.DelegateItem
import com.tinkoff.homework.presentation.view.ToChatRouter
import com.tinkoff.homework.presentation.view.adapter.AdapterDelegate

class TopicMessageDelegate(private val router: ToChatRouter) : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            TopicMessageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(item.content() as TopicModel)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is TopicMessageDelegateItem

    inner class ViewHolder(private val binding: TopicMessageItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(topic: TopicModel) {
            with(binding) {
                topicHeaderLayout.header.text = binding.root.context.getString(R.string.sharp, topic.topic)
                topicRoot.setOnClickListener{
                    router.goToChat(topic.topic, topic.streamName, topic.streamId)
                }
            }
        }
    }
}