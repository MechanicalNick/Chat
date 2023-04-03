package com.tinkoff.homework.data.dto

import com.tinkoff.homework.data.Status

class ProfileDto(
    val id: Long,
    val name: String,
    val status: Status,
    val avatarUrl: String?
)