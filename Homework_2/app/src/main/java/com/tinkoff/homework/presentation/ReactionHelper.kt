package com.tinkoff.homework.presentation

object ReactionHelper {
    fun emojiCodeToString(raw: String): String {
        var result = ""
        for (value in raw.split('-'))
            result += String(Character.toChars(value.toInt(16)))
        return result
    }
}