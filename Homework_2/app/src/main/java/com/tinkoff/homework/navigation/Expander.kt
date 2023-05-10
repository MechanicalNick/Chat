package com.tinkoff.homework.navigation

import com.tinkoff.homework.presentation.view.adapter.stream.StreamDelegateItem

interface Expander {
    fun expand(item: StreamDelegateItem)
    fun collapse(item: StreamDelegateItem)
}