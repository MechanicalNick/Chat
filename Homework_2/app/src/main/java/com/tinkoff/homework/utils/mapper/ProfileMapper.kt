package com.tinkoff.homework.utils.mapper

import com.tinkoff.homework.data.domain.Profile
import com.tinkoff.homework.data.dto.ProfileDto

fun toDomainProfile(dto: ProfileDto): Profile = dto.toDomain()

fun ProfileDto.toDomain(): Profile = Profile(
    id = id,
    name = name,
    description = description,
    status = status
)