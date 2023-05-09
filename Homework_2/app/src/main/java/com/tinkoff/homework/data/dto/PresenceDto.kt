package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PresenceDto(
    @field:Json(name = "website")
    val website: StatusDto,
    @field:Json(name = "aggregated")
    val aggregated: StatusDto
)