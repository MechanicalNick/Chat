package com.tinkoff.homework.elm.people.model

import android.os.Parcelable
import com.tinkoff.homework.data.domain.People
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class PeopleState(
    val item: @RawValue List<People>? = null,
    val error: Throwable? = null,
    val isLoading: Boolean = false
) : Parcelable