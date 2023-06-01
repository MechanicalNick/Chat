package com.tinkoff.homework.elm.profile.model

import com.tinkoff.homework.domain.data.Profile

sealed class ProfileEvent {

    sealed class Ui : ProfileEvent() {
        object Wait : Ui()
        data class LoadData(val profileId: Long) : Ui()
    }

    sealed class Internal : ProfileEvent() {

        data class DataLoaded(val profile: Profile) : Internal()

        data class ErrorLoading(val error: Throwable) : Internal()
    }
}