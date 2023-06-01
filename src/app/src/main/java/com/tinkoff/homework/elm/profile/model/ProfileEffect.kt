package com.tinkoff.homework.elm.profile.model

sealed class ProfileEffect {
    data class LoadError(val error: Throwable) : ProfileEffect()
}
