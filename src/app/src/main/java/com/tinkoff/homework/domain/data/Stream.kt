package com.tinkoff.homework.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Stream(
    val id: Long,
    val name: String,
    val topics: MutableList<Topic>,
    var isExpanded: Boolean
): Parcelable