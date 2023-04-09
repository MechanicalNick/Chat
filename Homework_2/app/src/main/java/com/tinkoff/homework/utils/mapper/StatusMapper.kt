package com.tinkoff.homework.utils.mapper

import com.tinkoff.homework.data.domain.Status

fun toStatus(status: String?): Status {
    return when (status) {
        "online" -> Status.Online
        "idle" -> Status.Idle
        else -> Status.Offline
    }
}