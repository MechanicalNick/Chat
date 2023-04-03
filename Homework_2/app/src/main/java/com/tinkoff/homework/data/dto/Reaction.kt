package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Reaction(
    @field:Json(name = "emoji_code")
    val emojiCode: String,
    @field:Json(name = "emoji_name")
    val emojiName: String,
    @field:Json(name = "user_id")
    val userId: Long
)