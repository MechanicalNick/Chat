package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UserDto(
    @field:Json(name = "user_id")
    val userId: Long,
    @field:Json(name = "full_name")
    val full_name: String,
    @field:Json(name = "delivery_email")
    val delivery_email: String?,
    @field:Json(name = "email")
    val email: String,
    @field:Json(name = "avatar_url")
    val avatarUrl: String?,
    @field:Json(name = "is_active")
    val isActive: Boolean,
    @field:Json(name = "is_bot")
    val is_bot: Boolean
)