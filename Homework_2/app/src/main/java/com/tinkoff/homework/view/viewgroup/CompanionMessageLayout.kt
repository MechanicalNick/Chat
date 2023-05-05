package com.tinkoff.homework.view.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import com.tinkoff.homework.R
import java.lang.Integer.max

class CompanionMessageLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    val avatarView: ImageView by lazy { findViewById(R.id.companionAvatarView) }
    val textMessage: TextView by lazy { findViewById(R.id.companionTextMessage) }
    val textName: TextView by lazy { findViewById(R.id.companionTextName) }
    val flexbox: FlexboxLayout by lazy { findViewById(R.id.companionFlexbox) }
    val userImage: ImageView by lazy { findViewById(R.id.companionUserImage) }

    private val cardView: View by lazy { findViewById(R.id.companionCardView) }

    private val marginBetweenImageAndFlexbox = 9
    private val marginBetweenCardAndFlexbox = 7

    init {
        inflate(context, R.layout.companion_message_layout_content, this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChild(widthMeasureSpec, heightMeasureSpec)

        var totalHeight = cardView.measuredHeight
        if(flexbox.measuredHeight > 0)
         totalHeight += marginBetweenCardAndFlexbox + flexbox.measuredHeight

        val totalWidth = avatarView.measuredWidth + marginBetweenImageAndFlexbox + max(
            flexbox.measuredWidth,
            cardView.measuredWidth
        )

        setMeasuredDimension(totalWidth, totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetX = 0
        var offsetY = 0

        avatarView.layout(
            0,
            0,
            avatarView.measuredWidth,
            avatarView.measuredHeight
        )

        offsetX += avatarView.measuredWidth + marginBetweenImageAndFlexbox

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