package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class StatusDto(
    @field:Json(name = "status")
    val status: String,
    @field:Json(name = "timestamp")
    val timestamp: Long,
)