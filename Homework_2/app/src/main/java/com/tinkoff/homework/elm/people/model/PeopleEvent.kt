package com.tinkoff.homework.elm.people.model

import com.tinkoff.homework.domain.data.People

sealed class PeopleEvent {

    sealed class Ui : PeopleEvent() {
        object LoadData : Ui()
        data class GoToProfile(val userId: Long) : Ui()
    }

    sealed class Internal : PeopleEvent() {

        data class DataLoaded(val peoples: List<People>) : Internal()

        data class ErrorLoading(val error: Throwable) : Internal()
    }
}