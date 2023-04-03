package com.tinkoff.homework.data.domain

import com.tinkoff.homework.data.Status

class Profile(
    val id: Int,
    val name: String,
    val description: String,
    val status: Status
)