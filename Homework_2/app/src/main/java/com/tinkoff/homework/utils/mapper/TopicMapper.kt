package com.tinkoff.homework.utils.mapper

import com.tinkoff.homework.data.domain.Topic
import com.tinkoff.homework.data.dto.TopicDto

fun toDomainTopic(dto: List<TopicDto>): List<Topic> = dto.map(TopicDto::toDomain)

private fun TopicDto.toDomain(): Topic = Topic(
    id = id,
    name = name,
    messageCount = messageCount,
    streamName = streamName
)
