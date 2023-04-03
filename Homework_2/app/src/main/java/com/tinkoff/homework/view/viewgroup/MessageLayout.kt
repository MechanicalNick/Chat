package com.tinkoff.homework.view.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import com.tinkoff.homework.R

class MessageLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    internal val textView: TextView by lazy { findViewById(R.id.textMessage) }
    internal val flexbox: FlexboxLayout by lazy { findViewById(R.id.flexbox) }

    private val cardView: View by lazy { findViewById(R.id.cardView) }
    private val marginBetweenCardAndFlexbox = 7

    init {
        inflate(context, R.layout.message_layout_content, this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChild(widthMeasureSpec, heightMeasureSpec)

        val totalHeight =
            cardView.measuredHeight + marginBetweenCardAndFlexbox + flexbox.measuredHeight
        val totalWidth = MeasureSpec.getSize(widthMeasureSpec)

        setMeasuredDimension(totalWidth, totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        cardView.layout(
            r - cardView.measuredWidth,
            0,
            r,
            cardView.measuredHeight
        )

        flexbox.layout(
            r - cardView.measuredWidth,
            cardView.measuredHeight + marginBetweenCardAndFlexbox,
            r,
            cardView.measuredHeight + marginBetweenCardAndFlexbox + flexbox.measuredHeight
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