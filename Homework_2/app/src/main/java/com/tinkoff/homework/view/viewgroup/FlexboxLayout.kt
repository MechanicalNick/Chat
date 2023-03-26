package com.tinkoff.homework.view.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import com.tinkoff.homework.R
import java.lang.Integer.max
import kotlin.math.ceil

class FlexboxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var maxCountInRow = 0
    private var margin = 0

    init {
        context.withStyledAttributes(attrs, R.styleable.FlexboxLayout) {
            maxCountInRow = this.getInt(R.styleable.FlexboxLayout_maxCountInRow, 0)
            margin = this.getInt(R.styleable.FlexboxLayout_flexboxMargin, 6)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChild(widthMeasureSpec, heightMeasureSpec)

        var currentRowWidth = 0
        var childrenInRow = 0
        var maxWidth = 0

        this.children.forEach { child ->
            if (childrenInRow == maxCountInRow) {
                maxWidth = max(maxWidth, currentRowWidth)
                childrenInRow = 0
                currentRowWidth = 0
            } else {
                childrenInRow++
            }
            if (childrenInRow != 0) {
                currentRowWidth += margin
            }
            currentRowWidth += child.measuredWidth
        }
        if (childrenInRow > 0) {
            maxWidth = max(maxWidth, currentRowWidth)
        }

        val rowCount = if (maxCountInRow > 0) ceil(this.childCount * 1.0 / maxCountInRow).toInt() else 0
        // Все элементы имеют одинаковую высоту
        val elementHeight = this.children.firstOrNull()?.measuredHeight ?: 0
        val totalHeight = rowCount * elementHeight + (rowCount - 1) * margin
        val totalWidth = if (rowCount == 1) currentRowWidth else maxWidth

        setMeasuredDimension(totalWidth, totalHeight)
    }

    private fun measureChild(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        this.children.forEach { child ->
            measureChildWithMargins(
                child, widthMeasureSpec, 0,
                heightMeasureSpec, 0
            )
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetX = 0
        var offsetY = 0
        var counter = 0
        this.children.forEach { child ->
            child.layout(
                offsetX,
                offsetY,
                offsetX + child.measuredWidth,
                offsetY + child.measuredHeight
            )

            offsetX += child.measuredWidth + margin
            counter++

            if (counter == maxCountInRow) {
                offsetX = 0
                offsetY += child.measuredHeight + margin
                counter = 0
            }
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
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