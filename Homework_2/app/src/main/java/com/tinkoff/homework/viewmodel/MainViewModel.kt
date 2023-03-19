package com.tinkoff.homework.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tinkoff.homework.data.EmojiWrapper

class MainViewModel : ViewModel() {
    val addEmoji: MutableLiveData<EmojiWrapper> = MutableLiveData()
    val removeEmoji: MutableLiveData<EmojiWrapper> = MutableLiveData()
}