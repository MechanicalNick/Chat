package com.tinkoff.homework.data.domain

import com.tinkoff.homework.data.Status

class Profile(
    val id: Long,
    val name: String,
    val avatarUrl: String?,
    val status: Status
)