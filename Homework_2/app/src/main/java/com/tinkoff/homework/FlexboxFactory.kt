package com.tinkoff.homework

import android.content.Context
import android.view.View

class FlexboxFactory(private val viewCount: Int, private val context: Context) {
    fun create(): List<View> {
        val views = mutableListOf<View>()
        for (i in 0 until viewCount) {
            val reactionView = ReactionView(context)
            reactionView.textToDraw = "🤪 ${i + 1}"
            views.add(reactionView)
        }

        if (views.isNotEmpty()) {
            val plusView = PlusView(context)
            views.add(plusView)
        }

        return views
    }
}