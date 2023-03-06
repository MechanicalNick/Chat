package com.tinkoff.homework

import android.content.Context
import android.util.AttributeSet

class PlusView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, R.attr.plusViewStyle) {

    private val width = 121
    private val height = 72

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }
}