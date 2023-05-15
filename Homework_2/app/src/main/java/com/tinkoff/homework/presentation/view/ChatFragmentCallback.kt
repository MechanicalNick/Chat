package com.tinkoff.homework.presentation.view

import com.tinkoff.homework.data.dto.Credentials
import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.domain.data.Reaction

interface ChatFragmentCallback {
    fun reactionChange(reaction: Reaction, message: MessageModel, senderId: Long)
    fun showActionSelectorDialog(id: Long, senderId: Long): Boolean
    fun showReactionDialog(id: Long, senderId: Long): Boolean
    fun getMyCredentials(): Credentials
}