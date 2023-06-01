package com.tinkoff.homework.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import vivid.money.elmslie.core.store.Store
import java.util.concurrent.TimeUnit

abstract class SearchViewModel<Event, Effect, State> : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    var searchQueryPublisher: PublishSubject<String> = PublishSubject.create()
    var store: Store<Event, Effect, State>? = null

    init {
        searchQueryPublisher
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .subscribeBy { searchQuery ->
                Log.e("QUERY", searchQuery)
                accept(searchQuery)
            }
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        store = null
        super.onCleared()
    }

    abstract fun accept(searchQuery : String)
}