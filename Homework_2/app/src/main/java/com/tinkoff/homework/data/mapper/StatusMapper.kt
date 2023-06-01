package com.tinkoff.homework.data.mapper

import com.tinkoff.homework.domain.data.Status

fun toStatus(status: String?): Status {
    return when (status) {
        "online" -> Status.Online
        "idle" -> Status.Idle
        else -> Status.Offline
    }
}