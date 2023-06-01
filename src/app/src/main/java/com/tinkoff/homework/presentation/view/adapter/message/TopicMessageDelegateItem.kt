package com.tinkoff.homework.presentation.view.adapter.message

import com.tinkoff.homework.domain.data.TopicModel
import com.tinkoff.homework.presentation.view.DelegateItem

class TopicMessageDelegateItem(
    val id: Long,
    private val topicModel: TopicModel
) : DelegateItem {
    override fun content(): Any = topicModel

    override fun id(): Long = id

    override fun compareToOther(other: DelegateItem): Boolean {
        val otherItem = (other as TopicMessageDelegateItem)
        val currentItem = content() as TopicModel
        return otherItem.topicModel.topic == currentItem.topic &&
                otherItem.topicModel.streamId == currentItem.streamId &&
                otherItem.topicModel.streamName == currentItem.streamName
    }
}