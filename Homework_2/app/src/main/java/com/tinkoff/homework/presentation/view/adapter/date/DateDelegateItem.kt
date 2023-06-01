package com.tinkoff.homework.presentation.view.adapter.date

import com.tinkoff.homework.domain.data.DateModel
import com.tinkoff.homework.presentation.view.DelegateItem

class DateDelegateItem(
    val id: Long,
    private val value: DateModel,
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Long = id

    override fun compareToOther(other: DelegateItem): Boolean {
        val otherItem = (other as DateDelegateItem).value
        val currentItem = content() as DateModel
        return otherItem.id == currentItem.id &&
                otherItem.date == currentItem.date
    }
}