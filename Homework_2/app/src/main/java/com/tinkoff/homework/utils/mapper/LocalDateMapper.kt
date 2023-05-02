package com.tinkoff.homework.utils.mapper

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

fun toLocalDate(timestamp: Long): LocalDate {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timestamp),
        TimeZone.getDefault().toZoneId()
    ).toLocalDate()
}
