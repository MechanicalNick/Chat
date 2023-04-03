package com.tinkoff.homework.utils.adapter.message

import com.tinkoff.homework.data.MessageModel
import com.tinkoff.homework.utils.DelegateItem

class CompanionMessageDelegateItem(
    override val id: Long,
    override val value: MessageModel
) : MessageDelegateItem {
    override fun content(): Any = value

    override fun id(): Long = id

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as CompanionMessageDelegateItem).value == content()
    }
}