package com.tinkoff.homework.domain.data

data class Emoji(
    val category: String,
    val name: String,
    val code: String
) {
    fun getCodeString() = String(Character.toChars(code.toInt(16)))
}
