package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PresenceResponse(
    @field:Json(name = "presence")
    val presence: PresenceDto
)