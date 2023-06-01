package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PeopleResponse(
    @field:Json(name = "members")
    val peoples: List<UserDto>
)