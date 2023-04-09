package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PresencesResponse(
    @field:Json(name = "presences")
    val presences: Map<String, PresencesDto>
)