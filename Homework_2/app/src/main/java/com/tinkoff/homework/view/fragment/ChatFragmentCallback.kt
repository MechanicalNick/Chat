package com.tinkoff.homework.view.fragment

import com.tinkoff.homework.data.Reaction

interface ChatFragmentCallback {
    fun reactionRemove(reaction: Reaction, messageId: Long)
    fun showBottomSheetDialog(id: Long): Boolean
}