package com.tinkoff.homework.utils

import com.tinkoff.homework.utils.adapter.stream.StreamDelegateItem

interface Expander {
    fun expand(item: StreamDelegateItem)
    fun collapse(item: StreamDelegateItem)
}