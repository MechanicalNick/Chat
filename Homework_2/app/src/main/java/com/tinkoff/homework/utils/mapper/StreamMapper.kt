package com.tinkoff.homework.utils.mapper

import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.data.domain.Topic
import com.tinkoff.homework.data.entity.StreamEntity
import com.tinkoff.homework.data.entity.TopicEntity
import com.tinkoff.homework.data.results.StreamResult

fun Stream.toEntity(isSubscribed: Boolean): StreamEntity {
    return StreamEntity(
        streamId = this.id,
        name = this.name,
        isSubscribed = isSubscribed
    )
}

fun Stream.toEntities(): List<TopicEntity> {
    return this.topics.map {
        TopicEntity(
            name = it.name,
            messageCount = it.messageCount,
            streamId = it.streamId
        )
    }.toList()
}

fun StreamResult.toDomain(): Stream {
    return Stream(
        id = this.streamEntity.streamId,
        name = this.streamEntity.name,
        topics = this.topics.map { topic ->
            topic.toDomain(this.streamEntity)
        }
            .toMutableList(),
        isExpanded = false
    )
}

fun TopicEntity.toDomain(streamEntity: StreamEntity): Topic {
    return Topic(
        name = this.name,
        messageCount = this.messageCount,
        streamName = streamEntity.name,
        streamId = streamEntity.streamId
    )
}