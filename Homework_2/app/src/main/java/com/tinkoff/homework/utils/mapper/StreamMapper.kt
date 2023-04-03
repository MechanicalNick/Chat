package com.tinkoff.homework.utils.mapper

import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.data.dto.StreamDto

fun toDomainStream(dto: List<StreamDto>): List<Stream> = dto.map(StreamDto::toDomain)
fun toDomainStream(dto: StreamDto): Stream = dto.toDomain()

fun StreamDto.toDomain(): Stream = Stream(
    id = id,
    name = name,
    topics = toDomainTopic(topics),
    isSubscribed = isSubscribed,
    isExpanded = isExpanded
)
