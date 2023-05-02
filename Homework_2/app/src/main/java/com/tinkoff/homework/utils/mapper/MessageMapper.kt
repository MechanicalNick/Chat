package com.tinkoff.homework.utils.mapper

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.data.domain.Reaction
import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.data.dto.MessageDto
import com.tinkoff.homework.data.dto.MessageResponse
import com.tinkoff.homework.data.dto.NarrowDto
import com.tinkoff.homework.data.entity.MessageEntity
import com.tinkoff.homework.data.entity.ReactionEntity
import com.tinkoff.homework.data.results.MessageResult
import com.tinkoff.homework.utils.Const
import java.time.LocalDate

fun toMessageEntity(domain: MessageModel, streamId: Long, topic: String): MessageEntity {
    return MessageEntity(
        id = domain.id,
        senderId = domain.senderId,
        senderFullName = domain.senderFullName,
        text = domain.text,
        date = domain.date,
        avatarUrl = domain.avatarUrl,
        streamId = streamId,
        topicName = topic
    )
}

fun toReactionEntity(domain: Reaction, messageId: Long): ReactionEntity {
    return ReactionEntity(
        id = 0,
        userId = domain.userId,
        emojiCode = domain.emojiCode,
        emojiName = domain.emojiName,
        messageId = messageId
    )
}

fun toMessageDomain(dto: MessageDto): MessageModel {
    return MessageModel(
        dto.id,
        dto.senderId,
        dto.senderFullName,
        dto.subject ?: "",
        dto.streamId ?: 0,
        dto.content,
        toLocalDate(dto.timestamp),
        dto.avatarUrl,
        dto.reactions.map { r ->
            Reaction(
                emojiCode = r.emojiCode,
                emojiName = r.emojiName,
                userId = r.userId
            )
        }.toMutableList()
    )
}

fun toDomainMessage(result: MessageResult): MessageModel {
    return MessageModel(
        id = result.messageEntity.id,
        senderId = result.messageEntity.senderId,
        senderFullName = result.messageEntity.senderFullName,
        subject = result.messageEntity.topicName,
        streamId = result.messageEntity.streamId,
        text = result.messageEntity.text,
        date = result.messageEntity.date,
        avatarUrl = result.messageEntity.avatarUrl,
        reactions = result.reactions.map { toDomainReaction(entity = it) }.toMutableList()
    )
}

fun toDomainReaction(entity: ReactionEntity): Reaction {
    return Reaction(
        emojiName = entity.emojiName,
        emojiCode = entity.emojiCode,
        userId = entity.userId
    )
}

fun toMyMessageEntity(credentials: Credentials, response: MessageResponse, streamId: Long, topic: String): MessageEntity {
    return MessageEntity(
        response.id,
        streamId,
        topic,
        credentials.id,
        credentials.fullName,
        response.msg,
        LocalDate.now(),
        credentials.avatar
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
