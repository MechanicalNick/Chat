package com.tinkoff.homework.utils.adapter.message

import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.utils.DelegateItem

class MyMessageDelegateItem(
    override val id: Long,
    override val value: MessageModel,
    private val count: Int
) : MessageDelegateItem {
    override fun content(): Any = value

    override fun id(): Long = id

    override fun compareToOther(other: DelegateItem): Boolean {
        val otherItem = (other as MyMessageDelegateItem).value
        val currentItem = content() as MessageModel
        return otherItem.id == currentItem.id &&
                otherItem.text == currentItem.text &&
                otherItem.reactions == currentItem.reactions &&
                otherItem.reactions.count() == count
    }
}