package com.tinkoff.homework.data.domain

class Profile(
    val id: Long,
    val name: String,
    val avatarUrl: String?,
    val status: Status
)