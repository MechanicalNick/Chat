package com.tinkoff.homework.presentation.view

interface ToChatRouter {
    fun goToChat(topicName: String, streamName: String, streamId: Long)
}