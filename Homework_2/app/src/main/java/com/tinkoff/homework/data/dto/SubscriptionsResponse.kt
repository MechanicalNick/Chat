package com.tinkoff.homework.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SubscriptionsResponse(
    @field:Json(name = "result")
    val result: String,
    @field:Json(name = "subscriptions")
    val streams: List<SubscriptionDto>
)