package com.tinkoff.homework.utils.adapter.message

import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.utils.DelegateItem

class MyMessageDelegateItem(
    override val id: Long,
    override val value: MessageModel
) : MessageDelegateItem {
    override fun content(): Any = value

    override fun id(): Long = id

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as MyMessageDelegateItem).value == content()
    }
}