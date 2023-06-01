package com.tinkoff.homework.data.mapper

import com.squareup.moshi.Moshi
import com.tinkoff.homework.domain.data.Stream
import com.tinkoff.homework.domain.data.Topic
import com.tinkoff.homework.data.db.entity.StreamEntity
import com.tinkoff.homework.data.db.entity.TopicEntity
import com.tinkoff.homework.data.db.entity.results.StreamResult
import com.tinkoff.homework.data.dto.SubscribeOnStreamDto

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
        topics = this.topics.mapIndexed{ index, topic ->
            topic.toDomain(index, this.streamEntity)
        }
            .toMutableList(),
        isExpanded = false
    )
}

fun TopicEntity.toDomain(index: Int, streamEntity: StreamEntity): Topic {
    return Topic(
        position = index,
        name = this.name,
        messageCount = this.messageCount,
        streamName = streamEntity.name,
        streamId = streamEntity.streamId
    )
}

fun toSubscription(
    moshi: Moshi,
    streamName: String
): String {
    val dto = SubscribeOnStreamDto(streamName)
    var adapter = moshi.adapter(SubscribeOnStreamDto::class.java)
    val str = adapter.toJson(dto)
    return "[$str]"
}