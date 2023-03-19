package com.tinkoff.homework.data

data class Emoji(
    val category: String,
    val name: String,
    val code: Int
) {
    fun getCodeString() = String(Character.toChars(code))
}
