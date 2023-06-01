package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SubscribeOnStreamDto(
    @field:Json(name = "name")
    val name: String
)