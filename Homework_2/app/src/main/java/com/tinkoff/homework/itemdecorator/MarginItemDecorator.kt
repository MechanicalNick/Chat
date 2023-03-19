package com.tinkoff.homework.itemdecorator

import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecorator(private val space: Int, private val orientation: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (orientation == LinearLayout.HORIZONTAL) {
            outRect.bottom = space
            outRect.right = space
            outRect.left = space
            outRect.top = space

        } else {
            outRect.bottom = space
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = space
            else
                outRect.top = 0
        }
    }
}