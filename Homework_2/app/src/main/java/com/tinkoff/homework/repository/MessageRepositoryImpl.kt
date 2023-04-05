package com.tinkoff.homework.repository

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.tinkoff.homework.App
import com.tinkoff.homework.data.MessageModel
import com.tinkoff.homework.data.Reaction
import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.data.dto.Narrow
import com.tinkoff.homework.use_cases.GetSearchResultsUseCase
import com.tinkoff.homework.utils.ZulipChatApi
import io.reactivex.Single
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

class MessageRepositoryImpl: MessageRepository {
    @Inject
    lateinit var api: ZulipChatApi

    @Inject
    lateinit var moshi: Moshi

    init {
        App.INSTANCE.appComponent.inject(this)
    }

    override fun getMessages(
        anchor: String,
        numBefore: Long,
        numAfter: Long,
        topic: String,
        streamId: Long,
        query: String
    ): Single<List<MessageModel>> {
        return api.getMessages(
            anchor,
            numBefore,
            numAfter,
            narrow(topic, streamId, query)
        ).map { message -> message.messages }.map { list ->
            list.map { m ->
                val date = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(m.timestamp),
                    TimeZone.getDefault().toZoneId())
                    .toLocalDate()
                MessageModel(
                    m.id,
                    m.senderId,
                    m.senderFullName,
                    m.content,
                    date,
                    m.avatarUrl,
                    m.reactions.map { r -> Reaction(r.emojiCode, r.emojiName, r.userId) }
                        .toMutableList()
                )
            }
        }
    }

    override fun search(query: String, topic: String, streamId: Long): Single<List<MessageModel>> {
        return GetSearchResultsUseCase(this).invoke(query, topic, streamId)
    }

    override fun addReaction(messageId: Long, emojiName: String): Single<MessageResponse> {
        return api.addReaction(messageId, emojiName)
    }

    override fun removeReaction(messageId: Long, emojiName: String): Single<MessageResponse> {
        return api.removeReaction(messageId, emojiName)
    }

    override fun sendMessage(
        streamId: Long,
        topic: String,
        message: String
    ): Single<MessageResponse> {
        return api.sendMessage(streamId, topic, message)
    }

    private fun narrow(
        topic: String,
        streamId: Long,
        query: String
    ): String {
        val list = mutableListOf<Narrow>()

        if (topic.isNotBlank())
            list.add(Narrow(operator = "topic", operand = topic))

        list.add(Narrow(operator = "stream", operand = streamId))

        if (query.isNotBlank())
            list.add(Narrow(operator = "search", operand = query))

        val type = Types.newParameterizedType(
            List::class.java,
            Narrow::class.java,
        )
        val moshi = Moshi.Builder().build()
        var adapter = moshi.adapter<List<Narrow>>(type)

        return adapter.toJson(list)
    }
}