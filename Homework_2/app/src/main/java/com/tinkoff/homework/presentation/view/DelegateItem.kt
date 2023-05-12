package com.tinkoff.homework.presentation.view

interface DelegateItem {
    fun content(): Any
    fun id(): Long
    fun compareToOther(other: DelegateItem): Boolean
}