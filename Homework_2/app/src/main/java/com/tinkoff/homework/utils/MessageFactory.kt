package com.tinkoff.homework.utils

import android.text.Editable
import com.tinkoff.homework.date.DateDelegateItem
import com.tinkoff.homework.date.DateModel
import com.tinkoff.homework.message.MessageDelegateItem
import com.tinkoff.homework.message.MessageModel
import java.time.LocalDate

class MessageFactory(private val adapter: MessageAdapter,
                     messages: List<MessageModel>) {
    private val items: MutableList<DelegateItem> = mutableListOf()
    private var lastDate: LocalDate = LocalDate.now()

    init {
        lastDate = messages.minOf { message -> message.date }
        val groups = messages
            .sortedBy { message -> message.date }
            .groupBy { message -> message.date }
        val dates = mutableListOf<DateModel>()

        groups.entries.forEachIndexed { index, group ->
            val dateModel = DateModel(index + 1, group.key)
            dates.add(dateModel)
            items.add(DateDelegateItem(dateModel.id, dateModel))
            group.value.forEach{
                items.add(MessageDelegateItem(it.id, it))
            }
        }

        adapter.submitList(items)
    }

    fun addText(text: Editable?){
        text.let {
            val id = items.count() + 1
            val now = LocalDate.now()

            if(lastDate != now){
                val newDateItem = DateDelegateItem(id, DateModel(id, now))
                items.add(newDateItem)
                lastDate = now
            }

            val item = MessageDelegateItem(id,
                MessageModel(id, it.toString(),
                    LocalDate.now()))
            items.add(item)
            adapter.submitList(items)
        }
    }

}