package com.tinkoff.homework.elm.profile

import com.tinkoff.homework.elm.ViewState
import com.tinkoff.homework.elm.profile.model.ProfileCommand
import com.tinkoff.homework.elm.profile.model.ProfileEffect
import com.tinkoff.homework.elm.profile.model.ProfileEvent
import com.tinkoff.homework.elm.profile.model.ProfileState
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ProfileReducer :
    DslReducer<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand>() {
    override fun Result.reduce(event: ProfileEvent): Any {
        return when (event) {
            is ProfileEvent.Internal.DataLoaded -> state {
                copy(
                    state = ViewState.ShowData,
                    item = event.profile,
                    error = null
                )
            }

            is ProfileEvent.Internal.ErrorLoading -> state {
                copy(
                    state = ViewState.Error,
                    item = null,
                    error = event.error
                )
            }

            is ProfileEvent.Ui.Wait -> {
                state {
                    copy(
                        state = ViewState.Loading,
                        item = null,
                        error = null
                    )
                }
            }

            is ProfileEvent.Ui.LoadData -> {
                state {
                    copy(
                        state = ViewState.Loading,
                        item = null,
                        error = null
                    )
                }
                commands { +ProfileCommand.LoadData(event.profileId) }
            }
        }
    }
}