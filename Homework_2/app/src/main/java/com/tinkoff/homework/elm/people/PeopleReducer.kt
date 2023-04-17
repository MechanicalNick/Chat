package com.tinkoff.homework.elm.people

import com.tinkoff.homework.elm.people.model.PeopleCommand
import com.tinkoff.homework.elm.people.model.PeopleEffect
import com.tinkoff.homework.elm.people.model.PeopleEvent
import com.tinkoff.homework.elm.people.model.PeopleState
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class PeopleReducer : DslReducer<PeopleEvent, PeopleState, PeopleEffect, PeopleCommand>() {
    override fun Result.reduce(event: PeopleEvent): Any {
        return when (event) {
            is PeopleEvent.Internal.DataLoaded -> state {
                copy(
                    isLoading = false,
                    item = event.peoples,
                    error = null
                )
            }
            is PeopleEvent.Internal.ErrorLoading -> state {
                copy(
                    isLoading = false,
                    item = null,
                    error = event.error
                )
            }
            is PeopleEvent.Ui.LoadData -> {
                state {
                    copy(
                        isLoading = true,
                        item = null,
                        error = null
                    )
                }
                commands { +PeopleCommand.LoadData }
            }
        }
    }
}