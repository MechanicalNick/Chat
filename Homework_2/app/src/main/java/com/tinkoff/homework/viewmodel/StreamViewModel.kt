package com.tinkoff.homework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.repository.StreamRepositoryImpl
import com.tinkoff.homework.repository.interfaces.StreamRepository
import com.tinkoff.homework.utils.DelegateItem
import com.tinkoff.homework.utils.StreamFactory
import com.tinkoff.homework.utils.UiState
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class StreamViewModel(private val onlySubscribed: Boolean) : ViewModel() {
    val searchState: LiveData<UiState<List<DelegateItem>>> get() = _searchState
    internal val factory = StreamFactory()

    private val allItems: MutableList<DelegateItem> = mutableListOf()
    private val allStreams: MutableList<Stream> = mutableListOf()

    private val streamRepository: StreamRepository = StreamRepositoryImpl()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val _searchState: MutableLiveData<UiState<List<DelegateItem>>> = MutableLiveData()
    private val searchQueryPublisher: PublishSubject<String> = PublishSubject.create()

    init {
        subscribeToSearchQuery()
    }

    private fun subscribeToSearchQuery() {
        searchQueryPublisher
            .distinctUntilChanged()
            .debounce(500L, TimeUnit.MILLISECONDS)
            .switchMapSingle { query ->
                search(query)
                    .onErrorReturn { UiState.Error(it.cause) }
            }
            .subscribeOn(Schedulers.computation())
            .subscribe({ state ->
                _searchState.postValue(state)
            }, { error ->
                _searchState.postValue(UiState.Error(error))
            })
            .addTo(compositeDisposable)
    }


    private fun search(query: String): Single<UiState<List<DelegateItem>>> {
        _searchState.postValue(UiState.Loading())

        return Single.just(allStreams).map { r ->
            var newSteams = r.filter { item -> item.name.contains(query, true) }
            var newItems = factory.updateDelegateItems(newSteams)
            UiState.Data(newItems)
        }
    }

    fun searchQuery(query: String) {
        searchQueryPublisher.onNext(query)
    }

    fun init() {
        _searchState.postValue(UiState.Loading())

        streamRepository.fetchResults(onlySubscribed, "")
            .subscribeOn(Schedulers.io())
            .subscribe({ list ->
                var newItems = factory.updateDelegateItems(list)
                allItems.clear()
                allStreams.clear()
                allItems.addAll(newItems)
                allStreams.addAll(list)
                _searchState.postValue(UiState.Data(newItems))
            }, { error ->
                _searchState.postValue(UiState.Error(error))
            })
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    class Factory(private val onlySubscribed: Boolean) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return StreamViewModel(onlySubscribed) as T
        }
    }
}