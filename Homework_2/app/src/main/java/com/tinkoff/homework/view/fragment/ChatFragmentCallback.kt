package com.tinkoff.homework.view.fragment

import com.tinkoff.homework.data.domain.Reaction

interface ChatFragmentCallback {
    fun reactionRemove(reaction: Reaction, messageId: Long, senderId: Long)
    fun showBottomSheetDialog(id: Long, senderId: Long): Boolean
}