package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageResponse(
    @field:Json(name = "msg")
    val msg: String,
    @field:Json(name = "result")
    val result: String,
    @field:Json(name = "uri")
    val uri: String
)