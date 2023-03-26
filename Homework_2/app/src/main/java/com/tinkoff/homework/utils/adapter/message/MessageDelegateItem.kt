package com.tinkoff.homework.utils.adapter.message

import com.tinkoff.homework.data.MessageModel
import com.tinkoff.homework.utils.DelegateItem

class MessageDelegateItem(
    val id: Int,
    private val value: MessageModel
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Int = id

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as MessageDelegateItem).value == content()
    }
}