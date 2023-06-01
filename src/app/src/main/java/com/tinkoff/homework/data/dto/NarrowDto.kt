package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class NarrowDto(
    @field:Json(name = "operator")
    val operator: String,
    @field:Json(name = "operand")
    val operand: Any
)