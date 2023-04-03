package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TopicResponse(
    @field:Json(name = "topics")
    val topics: List<Topic>
)