package com.tinkoff.homework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tinkoff.homework.data.domain.People
import com.tinkoff.homework.repository.PeopleRepository
import com.tinkoff.homework.repository.PeopleRepositoryImpl
import com.tinkoff.homework.utils.DelegateItem
import com.tinkoff.homework.utils.UiState
import com.tinkoff.homework.utils.adapter.PeopleAdapter
import com.tinkoff.homework.utils.mapper.toDomainPeople
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class PeoplesViewModel : ViewModel(){
    val state: LiveData<UiState<List<People>>> get() = _state

    private val repository: PeopleRepository = PeopleRepositoryImpl()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val _state: MutableLiveData<UiState<List<People>>> = MutableLiveData()

    fun init() {
        _state.postValue(UiState.Loading())

        repository.getPeoples()
            .subscribeOn(Schedulers.computation())
            .subscribe({
                _state.postValue(UiState.Data(toDomainPeople(it))) },{
                    _state.postValue(UiState.Error(it))
            })
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}