package com.tinkoff.homework.utils.adapter.topic

import com.tinkoff.homework.data.domain.Topic
import com.tinkoff.homework.utils.DelegateItem

class TopicDelegateItem(
    val id: Long,
    private val value: Topic,
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Long = id

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as TopicDelegateItem).value == content()
    }
}