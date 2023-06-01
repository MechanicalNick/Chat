package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MessageDto(
    @field:Json(name = "avatar_url")
    val avatarUrl: String,
    @field:Json(name = "content")
    val content: String,
    @field:Json(name = "id")
    val id: Long,
    @field:Json(name = "reactions")
    val reactions: List<ReactionDto>,
    @field:Json(name = "sender_email")
    val senderEmail: String,
    @field:Json(name = "sender_full_name")
    val senderFullName: String,
    @field:Json(name = "sender_id")
    val senderId: Long,
    @field:Json(name = "subject")
    val subject: String?,
    @field:Json(name = "stream_id")
    val streamId: Long?,
    @field:Json(name = "timestamp")
    val timestamp: Long,
)