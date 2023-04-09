package com.tinkoff.homework.elm.profile.model

sealed class ProfileCommand {
    data class LoadData(val profileId: Long?) : ProfileCommand()
}