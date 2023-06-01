package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SubscriptionDto(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "stream_id")
    val streamId: Long,
    @field:Json(name = "description")
    val description: String
)