package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ReactionsResponse(
    @field:Json(name = "id")
    val id: Long = -1,
    @field:Json(name = "msg")
    val msg: String,
    @field:Json(name = "result")
    val result: String,
)
