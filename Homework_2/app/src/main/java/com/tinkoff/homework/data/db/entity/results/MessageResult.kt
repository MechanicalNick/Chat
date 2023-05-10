package com.tinkoff.homework.data.db.entity.results

import androidx.room.Embedded
import androidx.room.Relation
import com.tinkoff.homework.data.db.entity.MessageEntity
import com.tinkoff.homework.data.db.entity.ReactionEntity

data class MessageResult(
    @Embedded
    val messageEntity: MessageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "messageId"
    )
    val reactions: List<ReactionEntity>
)