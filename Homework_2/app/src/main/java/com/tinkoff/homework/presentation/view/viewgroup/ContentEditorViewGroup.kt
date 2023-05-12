package com.tinkoff.homework.presentation.view.viewgroup

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.tinkoff.homework.R
import com.tinkoff.homework.utils.dp
import kotlin.math.max

class ContentEditorViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, R.attr.contentEditorStyle) {
    val arrowButton: View by lazy { findViewById(R.id.arrowButton) }
    val editText: EditText by lazy { findViewById(R.id.editTextView) }
    val plusButton: View by lazy { findViewById(R.id.plusButton) }

    private val textViewLeftMargin = 10.dp(context)
    private val textViewTopMargin = 6.dp(context)
    private val textViewRightMargin = 12.dp(context)
    private val buttonRightMargin = 16.dp(context)

    init {
        inflate(context, R.layout.content_editor_view_group, this)
        updateVisibility(editText.length())
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val length = s?.length ?: 0
                updateVisibility(length)
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChild(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val buttonMeasureWidth = max(arrowButton.measuredWidth, plusButton.measuredWidth)

        editText.layout(
            textViewLeftMargin,
            textViewTopMargin,
            r - buttonMeasureWidth - (textViewLeftMargin + textViewRightMargin + buttonRightMargin),
            textViewTopMargin + editText.measuredHeight
        )

        arrowButton.layout(
            r - (buttonRightMargin + arrowButton.measuredWidth),
            ((editText.measuredHeight - arrowButton.measuredHeight) * 0.5).toInt(),
            r - (buttonRightMargin),
            ((editText.measuredHeight - arrowButton.measuredHeight) * 0.5 + arrowButton.measuredHeight).toInt()
        )

        plusButton.layout(
            r - (buttonRightMargin + plusButton.measuredWidth),
            ((editText.measuredHeight - plusButton.measuredHeight) * 0.5).toInt(),
            r - (buttonRightMargin),
            ((editText.measuredHeight - plusButton.measuredHeight) * 0.5 + plusButton.measuredHeight).toInt()
        )
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

    private fun measureChild(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val list = listOf(arrowButton, plusButton).filter { it.visibility == View.VISIBLE }
        list.forEach { child ->
            measureChildWithMargins(
                child, widthMeasureSpec, 0,
                heightMeasureSpec, 0
            )
        }

        val width = max(arrowButton.measuredWidth, plusButton.measuredWidth)
        val height = max(arrowButton.measuredHeight, plusButton.measuredHeight)

        measureChildWithMargins(
            editText, widthMeasureSpec, width,
            heightMeasureSpec, height
        )
    }

    private fun updateVisibility(length: Int) {
        arrowButton.visibility = if (length > 0) VISIBLE else GONE
        plusButton.visibility = if (length == 0) VISIBLE else GONE
    }
}