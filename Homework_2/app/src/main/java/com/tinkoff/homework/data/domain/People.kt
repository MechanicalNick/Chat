package com.tinkoff.homework.data.domain

import com.tinkoff.homework.data.Status

class People(
    val name: String,
    val key: String,
    val email: String,
    var status: Status,
    val avatarUrl: String?
)