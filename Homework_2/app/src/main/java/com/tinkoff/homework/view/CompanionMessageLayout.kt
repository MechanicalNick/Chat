package com.tinkoff.homework.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.tinkoff.homework.R

class CompanionMessageLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private val image: View by lazy { findViewById(R.id.image) }
    private val cardView: View  by lazy { findViewById(R.id.cardView) }
    private val flexbox: FlexboxLayout  by lazy { findViewById(R.id.flexbox) }
    private val marginBetweenImageAndFlexbox = 9
    private val marginBetweenCardAndFlexbox = 7

    init {
        inflate(context, R.layout.companion_message_layout_content, this)

        val flexboxFactory = FlexboxFactory(33, this.context)
        flexboxFactory.create().forEach { view ->
            flexbox.addView(view)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChild(widthMeasureSpec, heightMeasureSpec)

        val totalHeight = cardView.measuredHeight + marginBetweenCardAndFlexbox + flexbox.measuredHeight
        val totalWidth = image.measuredWidth + marginBetweenImageAndFlexbox + flexbox.measuredWidth

        setMeasuredDimension(totalWidth, totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetX = 0
        var offsetY = 0

        image.layout(
            offsetX,
            offsetY,
            offsetX + image.measuredWidth,
            offsetY + image.measuredHeight
        )

        offsetX += image.measuredWidth + marginBetweenImageAndFlexbox
        cardView.layout(
            offsetX,
            offsetY,
            offsetX + cardView.measuredWidth,
            offsetY + cardView.measuredHeight
        )

        offsetY += cardView.measuredHeight + marginBetweenCardAndFlexbox
        flexbox.layout(
            offsetX,
            offsetY,
            offsetX + flexbox.measuredWidth,
            offsetY + flexbox.measuredHeight
        )
    }

    private fun measureChild(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        this.children.forEach { child ->
            measureChildWithMargins(
                child, widthMeasureSpec, 0,
                heightMeasureSpec, 0
            )
        }
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }
}