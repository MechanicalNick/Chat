package com.tinkoff.homework.elm.chat.model

import android.os.Parcelable
import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.elm.ViewState
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ChatState(
    val items: @RawValue List<MessageModel>? = null,
    val state: ViewState = ViewState.Loading,
    val itemsState: Boolean = false,
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val isShowProgress: Boolean = false,
    val needScroll: Boolean = false,
    val topicName: String = "",
    val streamId: Long = 0
) : Parcelable