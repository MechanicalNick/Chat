package com.tinkoff.homework.utils

import com.tinkoff.homework.data.domain.DateModel
import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.data.domain.Reaction
import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.domain.use_cases.interfaces.AddReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendMessageUseCase
import com.tinkoff.homework.utils.adapter.date.DateDelegateItem
import com.tinkoff.homework.utils.adapter.message.CompanionMessageDelegateItem
import com.tinkoff.homework.utils.adapter.message.MyMessageDelegateItem
import io.reactivex.Single
import java.time.LocalDate

class MessageFactory(
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) {
    private var items: MutableList<DelegateItem> = mutableListOf()
    private var lastDate: LocalDate = LocalDate.now()


    fun init(
        messages: List<MessageModel>,
        myId: Long
    ): MutableList<DelegateItem> {
        items = mutableListOf()

        if (messages.isNotEmpty()) {
            lastDate = messages
                .minOf { message -> message.date }
            val groups = messages
                .sortedBy { message -> message.date }
                .groupBy { message -> message.date }
            val dates = mutableListOf<DateModel>()

            groups.entries.forEachIndexed { index, group ->
                val dateModel = DateModel(index + 1L, group.key)
                dates.add(dateModel)
                items.add(DateDelegateItem(dateModel.id, dateModel))
                group.value.forEach {
                    val item = if (it.senderId == myId) MyMessageDelegateItem(it.id, it)
                    else CompanionMessageDelegateItem(it.id, it)
                    items.add(item)
                }
            }
        }
        return items
    }

    fun getCount(): Int = items.count()

    fun addReaction(messageId: Long, reaction: Reaction): Single<MessageResponse> {
        return addReactionUseCase.execute(messageId, reaction.emojiName)
    }

    fun removeReaction(messageId: Long, reaction: Reaction): Single<MessageResponse> {
        return removeReactionUseCase.execute(messageId, reaction.emojiName)
    }

    fun sendMessage(streamId: Long, topic: String, message: String): Single<MessageResponse> {
        return sendMessageUseCase.execute(streamId, topic, message)
    }
}