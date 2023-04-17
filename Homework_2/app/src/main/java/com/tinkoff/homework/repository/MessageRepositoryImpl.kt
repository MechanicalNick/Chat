package com.tinkoff.homework.repository

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.tinkoff.homework.App
import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.data.domain.Reaction
import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.data.dto.NarrowDto
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
                    m.reactions.map { r -> Reaction(
                        emojiCode = r.emojiCode,
                        emojiName = r.emojiName,
                        userId = r.userId
                    ) }.toMutableList()
                )
            }
        }
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
        val list = mutableListOf<NarrowDto>()

        if (topic.isNotBlank())
            list.add(NarrowDto(operator = "topic", operand = topic))

        list.add(NarrowDto(operator = "stream", operand = streamId))

        if (query.isNotBlank())
            list.add(NarrowDto(operator = "search", operand = query))

        val type = Types.newParameterizedType(
            List::class.java,
            NarrowDto::class.java,
        )
        val moshi = Moshi.Builder().build()
        var adapter = moshi.adapter<List<NarrowDto>>(type)

        return adapter.toJson(list)
    }
}