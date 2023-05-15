package com.tinkoff.homework.elm.channels.model

import android.os.Parcelable
import com.tinkoff.homework.domain.data.Stream
import com.tinkoff.homework.elm.ViewState
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ChannelsState(
    val items: @RawValue List<Stream>? = null,
    val error: Throwable? = null,
    val state: ViewState = ViewState.Loading,
    val isShowProgress: Boolean = false,
    val onlySubscribed: Boolean = false,
    // Библиотека не вызывает render(state: ChannelsState) при обновлении списка
    val flagForUpdateUi: Boolean = false
) : Parcelable