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
import com.tinkoff.homework.utils.Const
import java.time.LocalDateTime

fun MessageModel.toEntity(streamId: Long): MessageEntity {
    return MessageEntity(
        id = this.id,
        senderId = this.senderId,
        senderFullName = this.senderFullName,
        text = this.text,
        dateTime = this.dateTime,
        avatarUrl = this.avatarUrl,
        streamId = streamId,
        topicName = this.topic,
        imageUrl = toImageReference(this.text)
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
        id = this.id,
        senderId = this.senderId,
        senderFullName = this.senderFullName,
        topic = this.subject ?: "",
        streamId = this.streamId ?: 0,
        text = this.content,
        dateTime = toLocalDateTime(this.timestamp),
        avatarUrl = this.avatarUrl,
        reactions = this.reactions.map { r ->
            Reaction(
                emojiCode = r.emojiCode,
                emojiName = r.emojiName,
                userId = r.userId
            )
        }.toMutableList(),
        imageUrl = toImageReference(this.content)
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
        dateTime = this.messageEntity.dateTime,
        avatarUrl = this.messageEntity.avatarUrl,
        reactions = this.reactions.map { it.toDomain() }.toMutableList(),
        imageUrl = this.messageEntity.imageUrl
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
    val imageReference = toImageReference(this.msg)
    return MessageEntity(
        id = this.id,
        streamId = streamId,
        topicName = topic,
        senderId = credentials.id,
        senderFullName = credentials.fullName,
        text = this.msg,
        dateTime = LocalDateTime.now(),
        avatarUrl = credentials.avatar,
        imageUrl = imageReference
    )
}

fun toImageReference(message: String): String? {
    val isUserImageRegex = Regex(Const.IMAGE_PATTERN)
    val entire = isUserImageRegex.matchEntire(message)
    val match = (entire?.groups?.count() ?: 0) > 0
    return if(match)
        "${Const.SHORT_SITE}${entire!!.groups[3]!!.value}"
    else
        null
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
    val adapter = moshi.adapter<List<NarrowDto>>(type)

    return if (list.isEmpty()) null else adapter.toJson(list)
}
