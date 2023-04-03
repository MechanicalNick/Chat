package com.tinkoff.homework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tinkoff.homework.repository.StreamRepository
import com.tinkoff.homework.repository.StreamRepositoryImpl
import com.tinkoff.homework.utils.DelegateItem
import com.tinkoff.homework.utils.StreamFactory
import com.tinkoff.homework.utils.UiState
import com.tinkoff.homework.utils.mapper.toDomainStream
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.rx2.rxSingle
import java.util.concurrent.TimeUnit

class StreamViewModel(private val onlySubscribed: Boolean) : ViewModel() {
    internal val factory = StreamFactory()

    private var allItems: MutableList<DelegateItem> = mutableListOf()

    private val streamRepository: StreamRepository = StreamRepositoryImpl()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val _searchState: MutableLiveData<UiState<List<DelegateItem>>> = MutableLiveData()
    val searchState: LiveData<UiState<List<DelegateItem>>> get() = _searchState
    private val searchQueryPublisher: PublishSubject<String> = PublishSubject.create()

    init {
        subscribeToSearchQuery()
    }

    private fun subscribeToSearchQuery() {
        searchQueryPublisher
            .distinctUntilChanged()
            .debounce(500L, TimeUnit.MILLISECONDS)
            .switchMapSingle { query ->
                search(query, false)
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


    private fun search(query: String, isFirstSearch: Boolean): Single<UiState<List<DelegateItem>>> {
        _searchState.postValue(UiState.Loading())

        return when {
            query.isNotBlank() || isFirstSearch -> {
                streamRepository.search(query)
                    .map { request ->
                        val newItems =
                            factory.updateDelegateItems(request.streams.map { toDomainStream(it) })
                        if (isFirstSearch) {
                            allItems.addAll(newItems)
                        }
                        UiState.Data(newItems)
                    }
            }
            else -> rxSingle {
                factory.delegates.clear()
                factory.delegates.addAll(allItems)
                UiState.Data(allItems)
            }
        }
    }

    fun searchQuery(query: String) {
        searchQueryPublisher.onNext(query)
    }

    fun init() {
        _searchState.postValue(UiState.Loading())

        streamRepository.getResults(onlySubscribed)
            .subscribeOn(Schedulers.io())
            .subscribe({ list ->
                var newItems = factory.updateDelegateItems(list)
                allItems.addAll(newItems)
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