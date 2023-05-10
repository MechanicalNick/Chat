package com.tinkoff.homework.navigation

interface ToChatRouter {
    fun goToChat(topicName: String, streamName: String, streamId: Long)
}