package com.tinkoff.homework.utils.adapter.message

import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.utils.DelegateItem

interface MessageDelegateItem : DelegateItem {
    val id: Long
    val value: MessageModel
}