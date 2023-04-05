package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MessagesResponse(
    @field:Json(name = "messages")
    val messages: List<MessageDto>
)