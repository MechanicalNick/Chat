package com.tinkoff.homework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tinkoff.homework.data.domain.People
import com.tinkoff.homework.data.domain.Status
import com.tinkoff.homework.repository.PeopleRepository
import com.tinkoff.homework.repository.PeopleRepositoryImpl
import com.tinkoff.homework.utils.UiState
import io.reactivex.Single
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

        Single.zip(repository.getPeoples(), repository.getAllPresence()) { peoples, presences ->
            val res = peoples
                .map { peopleDto ->
                    with(peopleDto) {
                        status = getStatus(presences.presences[peopleDto.key]?.aggregated?.status)
                    }
                    peopleDto
                }
                .toList()
            res
        }.subscribeOn(Schedulers.computation())
            .subscribe({
                _state.postValue(UiState.Data(it))
            }, {
                _state.postValue(UiState.Error(it))
            })
            .addTo(compositeDisposable)
    }

    private fun getStatus(status: String?): Status {
        return when (status) {
            "online" -> Status.Online
            "idle" -> Status.Idle
            else -> Status.Offline
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}