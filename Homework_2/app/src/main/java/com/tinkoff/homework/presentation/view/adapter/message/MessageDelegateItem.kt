package com.tinkoff.homework.presentation.view.adapter.message

import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.navigation.DelegateItem

interface MessageDelegateItem : DelegateItem {
    val id: Long
    val value: MessageModel
}