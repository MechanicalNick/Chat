package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PresencesDto(
    @field:Json(name = "website")
    val website: ClientDto,
    @field:Json(name = "aggregated")
    val aggregated: ClientDto
)