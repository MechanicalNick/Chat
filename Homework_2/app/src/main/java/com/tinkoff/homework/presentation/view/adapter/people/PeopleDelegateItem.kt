package com.tinkoff.homework.presentation.view.adapter.people

import com.tinkoff.homework.domain.data.People
import com.tinkoff.homework.domain.data.Topic
import com.tinkoff.homework.presentation.view.DelegateItem

class PeopleDelegateItem(
    val id: Long,
    private val value: People,
) : DelegateItem {
    override fun content(): Any = value

    override fun id(): Long = id

    override fun compareToOther(other: DelegateItem): Boolean {
        val otherItem = (other as PeopleDelegateItem).value
        val currentItem = content() as People
        return otherItem.name == currentItem.name &&
                otherItem.email == currentItem.email
    }
}