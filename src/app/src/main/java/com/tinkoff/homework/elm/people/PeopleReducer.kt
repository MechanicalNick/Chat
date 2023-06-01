package com.tinkoff.homework.elm.people

import com.tinkoff.homework.elm.ViewState
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
                    state = ViewState.ShowData,
                    item = event.peoples,
                    error = null,
                    isShowProgress = false
                )
            }

            is PeopleEvent.Internal.ErrorLoading -> state {
                copy(
                    state = ViewState.Error,
                    item = null,
                    error = event.error
                )
            }

            is PeopleEvent.Ui.GoToProfile -> {
                effects { +PeopleEffect.GoToProfile(event.userId) }
            }

            is PeopleEvent.Ui.LoadData -> {
                state {
                    copy(
                        state = ViewState.Loading,
                        item = null,
                        error = null
                    )
                }
                commands { +PeopleCommand.LoadData }
            }
            is PeopleEvent.Ui.Search -> {
                state {
                    copy(
                        state = ViewState.ShowData,
                        item = null,
                        error = null,
                        isShowProgress = true
                    )
                }
                commands { +PeopleCommand.Search(event.query) }
            }
        }
    }
}