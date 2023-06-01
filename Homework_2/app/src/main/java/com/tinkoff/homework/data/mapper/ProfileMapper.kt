package com.tinkoff.homework.data.mapper

import com.tinkoff.homework.domain.data.Profile
import com.tinkoff.homework.data.dto.UserDto

fun UserDto.toDomainProfile(status: String): Profile = Profile(
    id = userId,
    name = full_name,
    avatarUrl = avatarUrl,
    status = toStatus(status)
)