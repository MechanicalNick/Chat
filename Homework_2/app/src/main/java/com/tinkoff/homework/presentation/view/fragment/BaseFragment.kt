package com.tinkoff.homework.presentation.view.fragment

import com.tinkoff.homework.elm.BaseStoreFactory
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder

abstract class BaseFragment<Event : Any, Effect : Any, State : Any> :
    ElmFragment<Event, Effect, State>() {

    abstract val factory: BaseStoreFactory<Event, Effect, State>

    override val storeHolder: StoreHolder<Event, Effect, State> by lazy {
        val store = factory.currentStore()
        store.stop()
        LifecycleAwareStoreHolder(lifecycle) {
            store
        }
    }
}