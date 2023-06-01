package com.tinkoff.homework.elm.profile

import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.profile.model.ProfileEffect
import com.tinkoff.homework.elm.profile.model.ProfileEvent
import com.tinkoff.homework.elm.profile.model.ProfileState
import vivid.money.elmslie.rx2.ElmStoreCompat

class ProfileStoreFactory(
    private val profileState: ProfileState,
    private val profileReducer: ProfileReducer,
    private val actor: ProfileActor
) : BaseStoreFactory<ProfileEvent, ProfileEffect, ProfileState> {
    private val store by lazy {
        ElmStoreCompat(
            initialState = profileState,
            reducer = profileReducer,
            actor = actor
        )
    }

    fun provide() = store
    override fun currentStore() = store
}