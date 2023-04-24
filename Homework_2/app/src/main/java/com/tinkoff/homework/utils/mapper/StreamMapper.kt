package com.tinkoff.homework.utils.mapper

import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.data.domain.Topic
import com.tinkoff.homework.data.entity.StreamEntity
import com.tinkoff.homework.data.entity.TopicEntity
import com.tinkoff.homework.data.results.StreamResult

fun toStreamEntity(domain: Stream, isSubscribed: Boolean): StreamEntity {
    return StreamEntity(
        streamId = domain.id,
        name = domain.name,
        isSubscribed = isSubscribed
    )
}

fun toTopicEntities(domain: Stream): List<TopicEntity> {
    return domain.topics.map {
        TopicEntity(
            id = 0,
            name = it.name,
            messageCount = it.messageCount,
            streamId = it.streamId
        )
    }.toList()
}

fun toDomainStream(streamResult: StreamResult): Stream {
    return Stream(
        id = streamResult.streamEntity.streamId,
        name = streamResult.streamEntity.name,
        topics = streamResult.topics.map { topic ->
            toDomainTopic(
                streamResult.streamEntity,
                topic
            )
        }
            .toMutableList(),
        isExpanded = false
    )
}

fun toDomainTopic(streamEntity: StreamEntity, topicEntity: TopicEntity): Topic {
    return Topic(
        name = topicEntity.name,
        messageCount = topicEntity.messageCount,
        streamName = streamEntity.name,
        streamId = streamEntity.streamId
    )
}