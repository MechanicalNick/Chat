package com.tinkoff.homework.presentation.viewmodel

import com.tinkoff.homework.elm.people.model.PeopleEffect
import com.tinkoff.homework.elm.people.model.PeopleEvent
import com.tinkoff.homework.elm.people.model.PeopleState

class PeopleViewModel : SearchViewModel<PeopleEvent, PeopleEffect, PeopleState>() {
    override fun accept(searchQuery: String) {
        store?.accept(PeopleEvent.Ui.Search(searchQuery))
    }
}