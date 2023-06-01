package com.tinkoff.homework.presentation.view

import com.tinkoff.homework.domain.data.DateModel
import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.data.TopicModel
import com.tinkoff.homework.presentation.view.adapter.date.DateDelegateItem
import com.tinkoff.homework.presentation.view.adapter.message.CompanionMessageDelegateItem
import com.tinkoff.homework.presentation.view.adapter.message.MyMessageDelegateItem
import com.tinkoff.homework.presentation.view.adapter.message.TopicMessageDelegateItem
import java.time.LocalDate

class MessageFactory {
    private var items: MutableList<DelegateItem> = mutableListOf()
    private var lastDate: LocalDate? = null
    private var lastTopic: String? = null
    private var currentMessages: List<MessageModel> = listOf()

    fun init(
        messages: List<MessageModel>,
        myId: Long,
        streamId: Long,
        streamName: String,
        needGroupByTopic: Boolean
    ): MutableList<DelegateItem> {
        items = mutableListOf()
        currentMessages = messages.toList()
        if (messages.isNotEmpty()){
            messages.sortedBy { message -> message.dateTime }.forEachIndexed { index, message ->

                val messageDate = message.dateTime.toLocalDate()
                if(lastDate == null || messageDate != lastDate) {
                    lastDate = message.dateTime.toLocalDate()
                    lastDate?.let {
                        val dateModel = DateModel(index + 1L, it)
                        items.add(DateDelegateItem(dateModel.id, dateModel))
                    }
                }

                if(needGroupByTopic) {
                    val group = message.topic
                    if (lastTopic == null || group != lastTopic) {
                        lastTopic = group
                        items.add(
                            TopicMessageDelegateItem(
                                index + 1L,
                                TopicModel(group, streamId, streamName)
                            )
                        )
                    }
                }

                addItem(message, myId)
            }
        }
        return items
    }

    private fun addItem(it: MessageModel, myId: Long) {
        items.add(
            if (it.senderId == myId) {
                MyMessageDelegateItem(it.id, it, it.reactions.count())
            }
            else {
                CompanionMessageDelegateItem(it.id, it, it.reactions.count())
            }
        )
    }

    fun getCount(): Int = items.count()
}