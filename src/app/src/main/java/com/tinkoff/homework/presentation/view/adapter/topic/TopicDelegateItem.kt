package com.tinkoff.homework.presentation.view.adapter.topic

import com.tinkoff.homework.domain.data.Topic
import com.tinkoff.homework.presentation.view.DelegateItem

class TopicDelegateItem(
    val id: Long,
    private val value: Topic,
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Long = id

    override fun compareToOther(other: DelegateItem): Boolean {
        val otherItem = (other as TopicDelegateItem).value
        val currentItem = content() as Topic
        return otherItem.name == currentItem.name &&
                otherItem.messageCount == currentItem.messageCount &&
                otherItem.streamId == currentItem.streamId
    }
}