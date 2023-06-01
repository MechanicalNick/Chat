package com.tinkoff.homework.presentation.view

import android.content.Context
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.tinkoff.homework.R
import com.tinkoff.homework.domain.data.Reaction
import com.tinkoff.homework.presentation.ReactionHelper
import com.tinkoff.homework.presentation.view.customview.PlusView
import com.tinkoff.homework.presentation.view.customview.ReactionView

class FlexboxFactory(
    private val reactions: List<Reaction>,
    private val context: Context,
    private val credentials: com.tinkoff.homework.data.dto.Credentials
) {

    fun create(
        reactionListener: (r: Reaction) -> Unit,
        showBottomSheetDialog: () -> Boolean
    ): List<View> {
        val map = HashMap<String, MutableList<Reaction>>()
        for(r in reactions){
            map.getOrPut(r.emojiCode) { mutableListOf() }.add(r)
        }

        val views = mutableListOf<View>()
        reactions.groupBy{it.emojiCode}.forEach { reactionGroup ->
            val reaction = reactionGroup.value.first()
            val reactionView = ReactionView(context)
            val emojiCode = ReactionHelper.emojiCodeToString(reaction.emojiCode)
            reactionView.textToDraw = "$emojiCode ${map[reaction.emojiCode]?.count()}"
            val myReaction = reactionGroup.value.firstOrNull { r -> r.userId == credentials.id}
            if(myReaction != null) {
                reactionView.background =
                    AppCompatResources.getDrawable(context, R.drawable.bg_reaction_my_view)
            }
            reactionView.setOnClickListener {
                reactionListener(reaction)
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