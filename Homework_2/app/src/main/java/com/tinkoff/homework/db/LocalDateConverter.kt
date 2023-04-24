package com.tinkoff.homework.db

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun fromCharSequence(value: String): LocalDate {
        return LocalDate.parse(value)
    }

    @TypeConverter
    fun localDateToCharSequence(date: LocalDate): String {
        return date.toString()
    }
}