package com.tinkoff.homework.elm.profile.model

import android.os.Parcelable
import com.tinkoff.homework.domain.data.Profile
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ProfileState(
    val item: @RawValue Profile? = null,
    val error: Throwable? = null,
    val isLoading: Boolean = false
) : Parcelable