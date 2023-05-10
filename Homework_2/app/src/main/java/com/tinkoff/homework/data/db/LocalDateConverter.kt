package com.tinkoff.homework.data.db

import androidx.room.TypeConverter
import com.tinkoff.homework.utils.Const
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateConverter {
    @TypeConverter
    fun fromCharSequence(value: String): LocalDate {
        return LocalDate.parse(value, DateTimeFormatter.ofPattern(Const.DATE_PATTERN))
    }

    @TypeConverter
    fun localDateToCharSequence(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofPattern(Const.DATE_PATTERN))
    }
}