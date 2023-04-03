package com.tinkoff.homework.utils.mapper

import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.data.dto.StreamDto

fun toDomainStream(dto: List<StreamDto>): List<Stream> = dto.map(StreamDto::toDomain)
fun toDomainStream(dto: StreamDto): Stream = dto.toDomain()

//fun requestToDomainStream(request: StreamResponse, isSubscribed: Boolean): List<Stream>{
//    return request.streams.map {stream -> stream.toDomainStream(isSubscribed)}
//}
fun com.tinkoff.homework.data.dto.Stream.toDomainStream(): Stream = Stream(
    id = streamId,
    name = name,
    topics = mutableListOf(),
    isExpanded = false
)

fun StreamDto.toDomain(): Stream = Stream(
    id = id,
    name = name,
    topics = toDomainTopic(topics, id),
    isExpanded = isExpanded
)
