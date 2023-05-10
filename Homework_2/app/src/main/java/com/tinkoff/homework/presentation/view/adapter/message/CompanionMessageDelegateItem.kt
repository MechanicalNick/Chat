package com.tinkoff.homework.presentation.view.adapter.message

import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.navigation.DelegateItem

class CompanionMessageDelegateItem(
    override val id: Long,
    override val value: MessageModel,
    private val count: Int
) : MessageDelegateItem {
    override fun content(): Any = value

    override fun id(): Long = id

    override fun compareToOther(other: DelegateItem): Boolean {
        val otherItem = (other as CompanionMessageDelegateItem).value
        val currentItem = content() as MessageModel
        return otherItem.id == currentItem.id &&
                otherItem.text == currentItem.text &&
                otherItem.reactions == currentItem.reactions &&
                otherItem.reactions.count() == count
    }
}