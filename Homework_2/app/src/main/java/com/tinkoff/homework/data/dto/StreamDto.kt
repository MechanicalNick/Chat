package com.tinkoff.homework.data.dto

class StreamDto(
    val id: Int,
    val name: String,
    val topics: List<TopicDto>,
    val isSubscribed: Boolean,
    var isExpanded: Boolean
)