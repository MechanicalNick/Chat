package com.tinkoff.homework.elm.people.model

sealed class PeopleCommand {
    object LoadData : PeopleCommand()
    data class Search(val query: String) : PeopleCommand()
}