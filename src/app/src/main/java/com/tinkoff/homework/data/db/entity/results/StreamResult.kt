package com.tinkoff.homework.data.db.entity.results

import androidx.room.Embedded
import androidx.room.Relation
import com.tinkoff.homework.data.db.entity.StreamEntity
import com.tinkoff.homework.data.db.entity.TopicEntity

data class StreamResult(
    @Embedded
    val streamEntity: StreamEntity,
    @Relation(
        parentColumn = "streamId",
        entityColumn = "streamId"
    )
    val topics: List<TopicEntity>
)