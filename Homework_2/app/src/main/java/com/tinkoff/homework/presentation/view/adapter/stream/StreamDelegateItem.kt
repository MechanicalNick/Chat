package com.tinkoff.homework.presentation.view.adapter.stream

import com.tinkoff.homework.domain.data.Stream
import com.tinkoff.homework.navigation.DelegateItem

class StreamDelegateItem(
    val id: Long,
    private val value: Stream,
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Long = id

    override fun compareToOther(other: DelegateItem): Boolean {
        val otherItem = (other as StreamDelegateItem).value
        val currentItem = content() as Stream
        return otherItem.id == currentItem.id &&
                otherItem.isExpanded == currentItem.isExpanded &&
                otherItem.name == currentItem.name
    }
}