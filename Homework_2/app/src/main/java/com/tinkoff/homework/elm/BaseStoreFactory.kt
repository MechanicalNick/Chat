package com.tinkoff.homework.elm

import vivid.money.elmslie.core.store.Store

interface BaseStoreFactory<Event : Any, Effect : Any, State : Any> {
    fun currentStore(): Store<Event, Effect, State>
}