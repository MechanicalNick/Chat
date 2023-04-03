package com.tinkoff.homework.utils.adapter.date

import com.tinkoff.homework.data.DateModel
import com.tinkoff.homework.utils.DelegateItem

class DateDelegateItem(
    val id: Long,
    private val value: DateModel,
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Long = id

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as DateDelegateItem).value == content()
    }
}