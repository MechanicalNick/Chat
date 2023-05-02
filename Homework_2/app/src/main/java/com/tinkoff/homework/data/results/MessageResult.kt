package com.tinkoff.homework.data.results

import androidx.room.Embedded
import androidx.room.Relation
import com.tinkoff.homework.data.entity.MessageEntity
import com.tinkoff.homework.data.entity.ReactionEntity

data class MessageResult(
    @Embedded
    val messageEntity: MessageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "messageId"
    )
    val reactions: List<ReactionEntity>
)