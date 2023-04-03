package com.tinkoff.homework.utils

interface ToChatRouter {
    fun goToChat(topicName: String, streamName: String, streamId: Long)
}