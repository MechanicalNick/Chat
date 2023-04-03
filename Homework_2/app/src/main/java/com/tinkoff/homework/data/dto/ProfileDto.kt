package com.tinkoff.homework.data.dto

import com.tinkoff.homework.data.Status

class ProfileDto(
    val id: Int,
    val name: String,
    val description: String,
    val status: Status
)