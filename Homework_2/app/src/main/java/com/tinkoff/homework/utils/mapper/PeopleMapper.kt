package com.tinkoff.homework.utils.mapper

import com.tinkoff.homework.data.domain.People
import com.tinkoff.homework.data.domain.Status
import com.tinkoff.homework.data.dto.UserDto

fun toDomainPeople(dto: List<UserDto>): List<People> = dto.map(UserDto::toDomain)


fun UserDto.toDomain(): People = People(
    name = full_name,
    key = email,
    email = delivery_email ?: email,
    status = Status.Offline,
    avatarUrl = avatarUrl,
    userId = userId
)