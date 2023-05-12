package com.tinkoff.homework.data.mapper

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.data.Reaction
import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.data.dto.MessageDto
import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.data.dto.NarrowDto
import com.tinkoff.homework.data.db.entity.MessageEntity
import com.tinkoff.homework.data.db.entity.ReactionEntity
import com.tinkoff.homework.data.db.entity.results.MessageResult
import java.time.LocalDate

fun MessageModel.toEntity(streamId: Long): MessageEntity {
    return MessageEntity(
        id = this.id,
        senderId = this.senderId,
        senderFullName = this.senderFullName,
        text = this.text,
        date = this.date,
        avatarUrl = this.avatarUrl,
        streamId = streamId,
        topicName = this.topic
    )
}

fun Reaction.toEntity(messageId: Long): ReactionEntity {
    return ReactionEntity(
        id = 0,
        userId = this.userId,
        emojiCode = this.emojiCode,
        emojiName = this.emojiName,
        messageId = messageId
    )
}

fun MessageDto.toDomain(): MessageModel {
    return MessageModel(
        this.id,
        this.senderId,
        this.senderFullName,
        this.subject ?: "",
        this.streamId ?: 0,
        this.content,
        toLocalDate(this.timestamp),
        this.avatarUrl,
        this.reactions.map { r ->
            Reaction(
                emojiCode = r.emojiCode,
                emojiName = r.emojiName,
                userId = r.userId
            )
        }.toMutableList()
    )
}

fun MessageResult.toDomain(): MessageModel {
    return MessageModel(
        id = this.messageEntity.id,
        senderId = this.messageEntity.senderId,
        senderFullName = this.messageEntity.senderFullName,
        topic = this.messageEntity.topicName,
        streamId = this.messageEntity.streamId,
        text = this.messageEntity.text,
        date = this.messageEntity.date,
        avatarUrl = this.messageEntity.avatarUrl,
        reactions = this.reactions.map { it.toDomain() }.toMutableList()
    )
}

fun ReactionEntity.toDomain(): Reaction {
    return Reaction(
        emojiName = this.emojiName,
        emojiCode = this.emojiCode,
        userId = this.userId
    )
}

fun MessageResponse.toMyMessageEntity(
    credentials: Credentials,
    streamId: Long,
    topic: String
): MessageEntity {
    return MessageEntity(
        id = this.id,
        streamId = streamId,
        topicName = topic,
        senderId = credentials.id,
        senderFullName = credentials.fullName,
        text = this.msg,
        date = LocalDate.now(),
        avatarUrl = credentials.avatar
    )
}

fun toNarrow(
    moshi: Moshi,
    topic: String,
    streamId: Long?,
    query: String
): String? {
    val list = mutableListOf<NarrowDto>()

    if (topic.isNotBlank())
        list.add(NarrowDto(operator = "topic", operand = topic))

    if (streamId != null)
        list.add(NarrowDto(operator = "stream", operand = streamId))

    if (query.isNotBlank())
        list.add(NarrowDto(operator = "search", operand = query))

    val type = Types.newParameterizedType(
        List::class.java,
        NarrowDto::class.java,
    )
    var adapter = moshi.adapter<List<NarrowDto>>(type)

    return if (list.isEmpty()) null else adapter.toJson(list)
}
