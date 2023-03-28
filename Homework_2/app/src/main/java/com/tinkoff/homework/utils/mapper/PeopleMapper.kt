package com.tinkoff.homework.utils.mapper

import com.tinkoff.homework.data.domain.People
import com.tinkoff.homework.data.dto.PeopleDto

fun toDomainPeople(dto: List<PeopleDto>): List<People> = dto.map(PeopleDto::toDomain)

fun PeopleDto.toDomain(): People = People(
   name = name,
   email = email,
   status = status
)