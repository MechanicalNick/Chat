package com.tinkoff.homework.presentation.view

import android.util.Log
import com.tinkoff.homework.domain.data.DateModel
import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.data.MessageResponseWrapper
import com.tinkoff.homework.domain.data.MessageResponseWrapperStatus
import com.tinkoff.homework.domain.data.Reaction
import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.domain.data.TopicModel
import com.tinkoff.homework.domain.use_cases.interfaces.AddReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.RemoveReactionUseCase
import com.tinkoff.homework.domain.use_cases.interfaces.SendMessageUseCase
import com.tinkoff.homework.presentation.view.adapter.date.DateDelegateItem
import com.tinkoff.homework.presentation.view.adapter.message.CompanionMessageDelegateItem
import com.tinkoff.homework.presentation.view.adapter.message.MyMessageDelegateItem
import com.tinkoff.homework.presentation.view.adapter.message.TopicMessageDelegateItem
import io.reactivex.Single
import java.time.LocalDate

class MessageFactory(
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val credentials: Credentials
) {
    private var items: MutableList<DelegateItem> = mutableListOf()
    private var lastDate: LocalDate = LocalDate.now()
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
        var topicId = 1L

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

                if(needGroupByTopic){
                    val topicGroup = group.value.groupBy { m -> m.topic }
                    topicGroup.forEach{ entry ->
                        items.add(TopicMessageDelegateItem(topicId++,
                            TopicModel(entry.key,streamId, streamName)))
                        entry.value.forEach{ message -> addItem(message, myId) }
                    }
                }
                else {
                    group.value.forEach { addItem(it, myId) }
                }

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

    fun changeReaction(messageId: Long, reaction: Reaction): Single<MessageResponseWrapper> {
        val message = currentMessages.first { message -> message.id == messageId }
        val currentReaction = message.reactions
            .firstOrNull { r -> r.emojiCode == reaction.emojiCode && r.userId == credentials.id }
        return if (currentReaction == null)
            addReaction(messageId, reaction)
                .map { MessageResponseWrapper(it, MessageResponseWrapperStatus.Added) }
        else
            removeReaction(messageId, reaction)
                .map { MessageResponseWrapper(it, MessageResponseWrapperStatus.Removed) }
    }

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