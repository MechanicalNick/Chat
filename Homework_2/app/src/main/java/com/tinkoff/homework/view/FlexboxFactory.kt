package com.tinkoff.homework.view

import android.content.Context
import android.view.View
import com.tinkoff.homework.data.Reaction

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