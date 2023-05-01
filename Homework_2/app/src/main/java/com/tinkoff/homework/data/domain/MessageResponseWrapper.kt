package com.tinkoff.homework.data.domain

import com.tinkoff.homework.data.dto.MessageResponse

class MessageResponseWrapper(val messageResponse: MessageResponse,
                             val status: MessageResponseWrapperStatus)

enum class MessageResponseWrapperStatus{
    Added,
    Removed
}