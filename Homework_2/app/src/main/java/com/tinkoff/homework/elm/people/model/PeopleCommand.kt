package com.tinkoff.homework.elm.people.model

sealed class PeopleCommand {
    object LoadData : PeopleCommand()
}