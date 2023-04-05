package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TopicDto(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "max_id")
    val maxId: Long
)