package com.tinkoff.homework.utils.adapter.stream

import com.tinkoff.homework.data.Stream
import com.tinkoff.homework.utils.DelegateItem

class StreamDelegateItem(
    val id: Int,
    private val value: Stream,
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Int = id

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as StreamDelegateItem).value == content()
    }
}