package com.tinkoff.homework.elm.people

import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.people.model.PeopleEffect
import com.tinkoff.homework.elm.people.model.PeopleEvent
import com.tinkoff.homework.elm.people.model.PeopleState
import vivid.money.elmslie.rx2.ElmStoreCompat

class PeopleStoreFactory(
    private val peopleState: PeopleState,
    private val peopleReducer: PeopleReducer,
    private val actor: PeopleActor
) : BaseStoreFactory<PeopleEvent, PeopleEffect, PeopleState> {
    private val store by lazy {
        ElmStoreCompat(
            initialState = peopleState,
            reducer = peopleReducer,
            actor = actor
        )
    }

    fun provide() = store
    override fun currentStore() = store
}