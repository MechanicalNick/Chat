package com.tinkoff.homework.view.fragment

import com.tinkoff.homework.data.Reaction

interface ChatFragmentCallback {
    fun reactionRemove(reaction: Reaction, messageId: Int)
    fun showBottomSheetDialog(id: Int): Boolean
}