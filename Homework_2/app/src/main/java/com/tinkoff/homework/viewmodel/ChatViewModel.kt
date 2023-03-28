package com.tinkoff.homework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tinkoff.homework.data.EmojiWrapper
import com.tinkoff.homework.data.MessageModel
import com.tinkoff.homework.data.domain.People
import com.tinkoff.homework.data.domain.Profile
import com.tinkoff.homework.repository.MessageRepository
import com.tinkoff.homework.repository.MessageRepositoryImpl
import com.tinkoff.homework.utils.UiState
import com.tinkoff.homework.utils.mapper.toDomainPeople
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class ChatViewModel : ViewModel() {
    val addEmoji: MutableLiveData<EmojiWrapper> = MutableLiveData()
    val removeEmoji: MutableLiveData<EmojiWrapper> = MutableLiveData()
    val state: LiveData<UiState<List<MessageModel>>> get() = _state

    private val repository: MessageRepository = MessageRepositoryImpl()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val _state: MutableLiveData<UiState<List<MessageModel>>> = MutableLiveData()

    fun init() {
        _state.postValue(UiState.Loading())

        repository.getMessages()
            .subscribeOn(Schedulers.computation())
            .subscribe({
                _state.postValue(UiState.Data(it)) },{
                _state.postValue(UiState.Error(it))
            })
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}