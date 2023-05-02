package com.tinkoff.homework.utils.adapter.topic

import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.data.domain.Topic
import com.tinkoff.homework.utils.DelegateItem
import com.tinkoff.homework.utils.adapter.stream.StreamDelegateItem

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