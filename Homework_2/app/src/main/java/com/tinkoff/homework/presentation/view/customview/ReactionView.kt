package com.tinkoff.homework.presentation.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.withStyledAttributes
import com.tinkoff.homework.R

class ReactionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, R.attr.reactionViewStyle, defStyleRes) {

    var textToDraw = ""
        set(value) {
            field = value
            invalidate()
        }


    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 14f.sp(context)
    }
    private var textBounds = Rect()

    init {

        context.withStyledAttributes(attrs, R.styleable.ReactionView) {
            val reaction = this.getString(R.styleable.ReactionView_reaction)
            val count = this.getInt(R.styleable.ReactionView_count, 0)
            textToDraw = "$reaction $count"
            setOnClickListener {
                it.isSelected = !it.isSelected
            }
        }
        textPaint.color = context.getColor(R.color.main_text)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(textToDraw, 0, textToDraw.length, textBounds)

        val textWidth = textBounds.width()
        val textHeight = textBounds.height()

        val measuredWidth = resolveSize(textWidth + paddingLeft + paddingRight, widthMeasureSpec)
        val measuredHeight = resolveSize(textHeight + paddingTop + paddingBottom, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val centerY = height / 2 - textBounds.exactCenterY()
        canvas.drawText(textToDraw, paddingLeft.toFloat(), centerY, textPaint)
    }

    fun getText(): CharSequence {
        return textToDraw
    }

    private fun Float.sp(context: Context) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        context.resources.displayMetrics
    )
}