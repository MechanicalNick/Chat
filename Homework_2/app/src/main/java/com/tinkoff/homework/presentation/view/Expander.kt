package com.tinkoff.homework.presentation.view

import com.tinkoff.homework.presentation.view.adapter.stream.StreamDelegateItem

interface Expander {
    fun expand(item: StreamDelegateItem)
    fun collapse(item: StreamDelegateItem)
}