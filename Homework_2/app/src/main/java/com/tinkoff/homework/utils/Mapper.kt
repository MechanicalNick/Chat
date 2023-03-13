package com.tinkoff.homework.utils

import com.tinkoff.homework.date.DateDelegateItem
import com.tinkoff.homework.date.DateModel
import com.tinkoff.homework.message.MessageDelegateItem
import com.tinkoff.homework.message.MessageModel

fun List<MessageModel>.concatenateWithDate(dates: List<DateModel>): List<DelegateItem> {
    val delegateItemList: MutableList<DelegateItem> = mutableListOf()

    dates.forEach { dateModel ->

        delegateItemList.add(
            DateDelegateItem(id = dateModel.id, value = dateModel)
        )

        val date = dateModel.date

        val allDayExpenses = this.filter { expense ->
            expense.date == date
        }

        allDayExpenses.forEach { model ->
            delegateItemList.add(
                MessageDelegateItem(
                    id = model.id,
                    value = model,
                )
            )
        }
    }
    return delegateItemList
}