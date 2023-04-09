package com.tinkoff.homework.data.dto

import com.tinkoff.homework.data.domain.Status

class ProfileDto(
    val id: Long,
    val name: String,
    val status: Status,
    val avatarUrl: String?
)