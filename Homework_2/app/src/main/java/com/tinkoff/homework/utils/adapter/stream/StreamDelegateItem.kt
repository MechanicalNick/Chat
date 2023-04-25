package com.tinkoff.homework.utils.adapter.stream

import com.tinkoff.homework.data.domain.DateModel
import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.utils.DelegateItem
import com.tinkoff.homework.utils.adapter.date.DateDelegateItem

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