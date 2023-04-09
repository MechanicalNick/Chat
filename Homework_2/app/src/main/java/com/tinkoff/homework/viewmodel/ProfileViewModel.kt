package com.tinkoff.homework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tinkoff.homework.data.domain.Profile
import com.tinkoff.homework.repository.ProfileRepository
import com.tinkoff.homework.repository.ProfileRepositoryImpl
import com.tinkoff.homework.utils.UiState
import com.tinkoff.homework.utils.mapper.toDomainProfile
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(private val profileId: Long?): ViewModel() {
    val state: LiveData<UiState<Profile>> get() = _state

    private val repository: ProfileRepository = ProfileRepositoryImpl()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val _state: MutableLiveData<UiState<Profile>> = MutableLiveData()

    fun init() {
        _state.postValue(UiState.Loading())

        repository.getProfile(profileId)
            .subscribeOn(Schedulers.computation())
            .subscribe({
                _state.postValue(UiState.Data(toDomainProfile(it))) },{
                _state.postValue(UiState.Error(it))
            })
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    class Factory(private val profileId: Long?) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(profileId) as T
        }
    }
}