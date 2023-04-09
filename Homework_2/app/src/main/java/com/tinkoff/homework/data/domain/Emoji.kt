package com.tinkoff.homework.data.domain

data class Emoji(
    val category: String,
    val name: String,
    val code: String
) {
    fun getCodeString() = String(Character.toChars(code.toInt(16)))
}
