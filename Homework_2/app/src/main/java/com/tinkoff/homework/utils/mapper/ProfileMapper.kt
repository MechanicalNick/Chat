package com.tinkoff.homework.utils.mapper

import com.tinkoff.homework.data.domain.Profile
import com.tinkoff.homework.data.dto.UserDto

fun UserDto.toDomainProfile(status: String): Profile = Profile(
    id = userId,
    name = full_name,
    avatarUrl = avatarUrl,
    status = toStatus(status)
)