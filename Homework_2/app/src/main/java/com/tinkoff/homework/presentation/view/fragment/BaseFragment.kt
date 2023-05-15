package com.tinkoff.homework.presentation.view.fragment

import android.view.ViewGroup
import androidx.core.view.isVisible
import com.facebook.shimmer.ShimmerFrameLayout
import com.tinkoff.homework.elm.BaseStoreFactory
import io.reactivex.disposables.CompositeDisposable
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder

abstract class BaseFragment<Event : Any, Effect : Any, State : Any> :
    ElmFragment<Event, Effect, State>() {
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    abstract val factory: BaseStoreFactory<Event, Effect, State>

    override val storeHolder: StoreHolder<Event, Effect, State> by lazy {
        val store = factory.currentStore()
        store.stop()
        LifecycleAwareStoreHolder(lifecycle) {
            store
        }
    }


    fun renderLoadingState(
        shimmerFrameLayout: ShimmerFrameLayout,
        errorContainer: ViewGroup,
        data: ViewGroup
    ) {
        shimmerFrameLayout.showShimmer(true)
        data.isVisible = false
        errorContainer.isVisible = false
    }

    fun renderErrorState(
        shimmerFrameLayout: ShimmerFrameLayout,
        errorContainer: ViewGroup,
        data: ViewGroup
    ) {
        shimmerFrameLayout.stopShimmer()
        data.isVisible = false
        errorContainer.isVisible = true
    }

    fun renderDataState(
        shimmerFrameLayout: ShimmerFrameLayout,
        errorContainer: ViewGroup,
        data: ViewGroup
    ) {
        shimmerFrameLayout.stopShimmer()
        data.isVisible = true
        errorContainer.isVisible = false
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }
}