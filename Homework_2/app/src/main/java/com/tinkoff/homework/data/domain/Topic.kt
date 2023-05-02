package com.tinkoff.homework.data.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Topic(
    val name: String,
    val messageCount: Long,
    val streamName: String,
    val streamId: Long,
): Parcelable