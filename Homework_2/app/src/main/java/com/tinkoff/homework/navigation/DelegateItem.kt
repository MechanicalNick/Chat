package com.tinkoff.homework.navigation

interface DelegateItem {
    fun content(): Any
    fun id(): Long
    fun compareToOther(other: DelegateItem): Boolean
}