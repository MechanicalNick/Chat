package com.tinkoff.homework.elm.people.model

sealed class PeopleEffect {
    data class LoadError(val error: Throwable) : PeopleEffect()
}
