package com.tinkoff.homework.data.mapper

import java.time.Instant
import java.time.LocalDateTime
import java.util.TimeZone

fun toLocalDateTime(timestamp: Long): LocalDateTime {
    return LocalDateTime
        .ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getDefault().toZoneId())
}
