package com.tinkoff.homework.domain.data

import com.tinkoff.homework.data.dto.MessageResponse

class MessageResponseWrapper(val messageResponse: MessageResponse,
                             val status: MessageResponseWrapperStatus
)

enum class MessageResponseWrapperStatus{
    Added,
    Removed
}