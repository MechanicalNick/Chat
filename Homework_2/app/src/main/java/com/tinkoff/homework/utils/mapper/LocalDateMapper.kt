package com.tinkoff.homework.utils.mapper

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.TimeZone

fun toLocalDate(timestamp: Long): LocalDate {
    return LocalDateTime
        .ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getDefault().toZoneId())
        .toLocalDate()
}
