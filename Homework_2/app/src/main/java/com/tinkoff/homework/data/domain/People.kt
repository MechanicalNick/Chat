package com.tinkoff.homework.data.domain

class People(
    val name: String,
    val key: String,
    val email: String,
    var status: Status,
    val avatarUrl: String?,
    val userId: Long
)