package com.tinkoff.homework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tinkoff.homework.data.EmojiWrapper
import com.tinkoff.homework.data.MessageModel
import com.tinkoff.homework.repository.MessageRepository
import com.tinkoff.homework.repository.MessageRepositoryImpl
import com.tinkoff.homework.utils.Const
import com.tinkoff.homework.utils.UiState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate

class ChatViewModel : ViewModel() {
    val state: LiveData<UiState<List<MessageModel>>> get() = _state
    val addEmoji: MutableLiveData<EmojiWrapper> = MutableLiveData()
    val removeEmoji: MutableLiveData<EmojiWrapper> = MutableLiveData()

    private val repository: MessageRepository = MessageRepositoryImpl()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val _state: MutableLiveData<UiState<List<MessageModel>>> = MutableLiveData()
    private val messages = mutableListOf<MessageModel>()

    fun init(topicName: String, streamId: Long) {
        _state.postValue(UiState.Loading())

        repository.getMessages("newest", 1000, 0, topicName, streamId, "")
            .subscribeOn(Schedulers.io())
            .subscribe({
                _state.postValue(UiState.Data(it))
                messages.clear()
                messages.addAll(it)
            }, {
                _state.postValue(UiState.Error(it))
            })
            .addTo(compositeDisposable)
    }

    fun addEmoji(messageId: Long, emojiCode: String, emojiName: String) {
        repository.addReaction(messageId, emojiName)
            .subscribeOn(Schedulers.io())
            .subscribe({
                addEmoji.postValue(EmojiWrapper(emojiCode, emojiName, messageId))
            }, {
                _state.postValue(UiState.Error(it))
            })
            .addTo(compositeDisposable)
    }

    fun removeEmoji(messageId: Long, emojiCode: String, emojiName: String) {
        repository.removeReaction(messageId, emojiName)
            .subscribeOn(Schedulers.io())
            .subscribe({
                removeEmoji.postValue(EmojiWrapper(emojiCode, emojiName, messageId))
            }, {
                _state.postValue(UiState.Error(it))
            })
            .addTo(compositeDisposable)
    }

    fun sendMessage(streamId: Long, topic: String, message: String) {
        repository.sendMessage(streamId, topic, message)
            .subscribeOn(Schedulers.io())
            .subscribe({
                val message = MessageModel(
                    it.id,
                    Const.myId,
                    Const.myFullName,
                    message,
                    LocalDate.now(),
                    Const.myAvatar,
                    mutableListOf()
                )
                messages.add(message)
                _state.postValue(UiState.Data(messages))
            }, {
                _state.postValue(UiState.Error(it))
            })
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}