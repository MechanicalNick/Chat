package com.tinkoff.homework.presentation.view.fragment

import com.tinkoff.homework.domain.data.Reaction

interface ChatFragmentCallback {
    fun reactionChange(reaction: Reaction, messageId: Long, senderId: Long)
    fun showBottomSheetDialog(id: Long, senderId: Long): Boolean
}