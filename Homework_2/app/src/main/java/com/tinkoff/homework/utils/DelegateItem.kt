package com.tinkoff.homework.utils

interface DelegateItem {
    fun content(): Any
    fun id(): Long
    fun compareToOther(other: DelegateItem): Boolean
}