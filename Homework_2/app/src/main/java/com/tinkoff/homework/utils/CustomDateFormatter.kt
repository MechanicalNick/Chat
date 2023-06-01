package com.tinkoff.homework.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField


object CustomDateFormatter {
    private val shortMonths = mapOf(
        1L to "Янв",
        2L to "Фев",
        3L to "Мар",
        4L to "Апр",
        5L to "Мая",
        6L to "Июн",
        7L to "Июл",
        8L to "Авг",
        9L to "Сен",
        10L to "Окт",
        11L to "Ноя",
        12L to "Дек",
    )

    fun formatDate(localDate: LocalDate): String? {
        val formatter = DateTimeFormatterBuilder()
            .appendPattern("d ")
            .appendText(ChronoField.MONTH_OF_YEAR, shortMonths)
            .toFormatter()
        return localDate.format(formatter)
    }
}