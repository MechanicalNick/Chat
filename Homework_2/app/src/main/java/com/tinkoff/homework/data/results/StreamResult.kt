package com.tinkoff.homework.data.results

import androidx.room.Embedded
import androidx.room.Relation
import com.tinkoff.homework.data.entity.StreamEntity
import com.tinkoff.homework.data.entity.TopicEntity

data class StreamResult(
    @Embedded
    val streamEntity: StreamEntity,
    @Relation(
        parentColumn = "streamId",
        entityColumn = "streamId"
    )
    val topics: List<TopicEntity>
)