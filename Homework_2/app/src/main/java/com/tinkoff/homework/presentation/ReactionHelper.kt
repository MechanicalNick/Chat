package com.tinkoff.homework.presentation

object ReactionHelper {
    fun emojiCodeToString(raw: String): String {
        var result = ""
        for (value in raw.split('-')) {
            val int = value.toIntOrNull(16)
            int?.let { result += String(Character.toChars(it)) }
        }
        return result
    }
}