package com.tinkoff.homework.elm.profile

import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.elm.profile.model.ProfileCommand
import com.tinkoff.homework.elm.profile.model.ProfileEffect
import com.tinkoff.homework.elm.profile.model.ProfileEvent
import com.tinkoff.homework.elm.profile.model.ProfileState
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ProfileReducer(private val credentials: Credentials) :
    DslReducer<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand>() {
    override fun Result.reduce(event: ProfileEvent): Any {
        return when (event) {
            is ProfileEvent.Internal.DataLoaded -> state {
                copy(
                    isLoading = false,
                    item = event.profile,
                    error = null
                )
            }

            is ProfileEvent.Internal.ErrorLoading -> state {
                copy(
                    isLoading = false,
                    item = null,
                    error = event.error
                )
            }
            is ProfileEvent.Ui.LoadData -> {
                state {
                    copy(
                        isLoading = true,
                        item = null,
                        error = null
                    )
                }
                commands { +ProfileCommand.LoadData(credentials.id) }
            }
        }
    }
}