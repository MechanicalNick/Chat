package com.tinkoff.homework.utils.mapper

import com.tinkoff.homework.data.domain.Topic
import com.tinkoff.homework.data.dto.TopicDto

fun toDomainTopic(dto: List<TopicDto>, streamId: Long): MutableList<Topic> =
    dto.map{d -> d.toDomain(streamId)}.toMutableList()

private fun TopicDto.toDomain(streamId: Long): Topic = Topic(
    name = name,
    messageCount = messageCount,
    streamName = streamName,
    streamId = streamId
)
