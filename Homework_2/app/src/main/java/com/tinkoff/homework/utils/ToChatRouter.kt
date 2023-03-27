package com.tinkoff.homework.utils

interface ToChatRouter {
    fun goToChat(id: Int, topicName: String, streamName: String)
}