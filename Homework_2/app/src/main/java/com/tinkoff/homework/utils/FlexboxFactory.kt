package com.tinkoff.homework.utils

import android.content.Context
import android.view.View
import com.tinkoff.homework.data.Reaction
import com.tinkoff.homework.view.PlusView
import com.tinkoff.homework.view.ReactionView

class FlexboxFactory(private val reactions: List<Reaction>, private val context: Context) {
    fun create(
        reactionListner: (r: Reaction) -> Unit,
        showBottomSheetDialog: () -> Boolean
    ): List<View> {
        val views = mutableListOf<View>()
        reactions.forEach { reaction ->
            val reactionView = ReactionView(context)
            reactionView.textToDraw =
                "${String(Character.toChars(reaction.code))}${reaction.owners.count()}"
            reactionView.setOnClickListener {
                reactionListner(reaction)
            }

            views.add(reactionView)
        }

        if (views.isNotEmpty()) {
            val plusView = PlusView(context)
            views.add(plusView)
            plusView.setOnClickListener {
                showBottomSheetDialog()
            }
        }

        return views
    }
}