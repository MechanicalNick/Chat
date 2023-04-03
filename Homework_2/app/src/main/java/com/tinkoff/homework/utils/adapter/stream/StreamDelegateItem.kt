package com.tinkoff.homework.utils.adapter.stream

import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.utils.DelegateItem

class StreamDelegateItem(
    val id: Long,
    private val value: Stream,
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Long = id

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as StreamDelegateItem).value == content()
    }
}