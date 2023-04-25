package com.tinkoff.homework.utils.mapper

import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.data.domain.Reaction
import com.tinkoff.homework.data.dto.MessageDto
import com.tinkoff.homework.data.dto.MessageResponse
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

fun toMyMessageEntity(response: MessageResponse, streamId: Long, topic: String): MessageEntity {
    return MessageEntity(
        response.id,
        streamId,
        topic,
        Const.myId,
        Const.myFullName,
        response.msg,
        LocalDate.now(),
        Const.myAvatar
    )
}
