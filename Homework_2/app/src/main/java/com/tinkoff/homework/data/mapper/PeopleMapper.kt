package com.tinkoff.homework.data.mapper

import com.tinkoff.homework.domain.data.People
import com.tinkoff.homework.domain.data.Status
import com.tinkoff.homework.data.dto.UserDto

fun UserDto.toDomain(): People = People(
    name = full_name,
    key = email,
    email = delivery_email ?: email,
    status = Status.Offline,
    avatarUrl = avatarUrl,
    userId = userId
)