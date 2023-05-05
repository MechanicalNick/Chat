package com.tinkoff.homework.view.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.withStyledAttributes
import com.tinkoff.homework.R

class SearchViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, R.attr.searchViewGroupStyle) {
    val searchText: EditText by lazy { findViewById(R.id.search_text) }
    private val searchView: View by lazy { findViewById(R.id.search_icon) }

    init {
        inflate(context, R.layout.search_viewgroup, this)
        context.withStyledAttributes(attrs, R.styleable.SearchViewGroup) {
            getString(R.styleable.SearchViewGroup_hint)?.let {
                searchText.hint = it
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val searchViewLayoutParam = searchView.layoutParams as MarginLayoutParams

        measureChildWithMargins(
            searchView, widthMeasureSpec, 0,
            heightMeasureSpec, 0
        )

        val width = searchView.measuredWidth + searchViewLayoutParam.leftMargin + searchViewLayoutParam.rightMargin
        val height = searchView.measuredHeight + searchViewLayoutParam.topMargin + searchViewLayoutParam.bottomMargin

        measureChildWithMargins(
            searchText, widthMeasureSpec, width,
            heightMeasureSpec, height
        )

        setMeasuredDimension(
            resolveSize(searchView.width + searchText.width, widthMeasureSpec),
            resolveSize(searchView.height + searchText.height, heightMeasureSpec)
        )
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val searchTextLayoutParam = searchText.layoutParams as MarginLayoutParams
        val searchViewLayoutParam = searchView.layoutParams as MarginLayoutParams

        searchText.layout(
            searchTextLayoutParam.leftMargin,
            searchTextLayoutParam.topMargin,
            searchTextLayoutParam.leftMargin + searchText.measuredWidth,
            searchTextLayoutParam.topMargin + searchText.measuredHeight
        )
        searchView.layout(
            r - searchViewLayoutParam.rightMargin - searchView.measuredWidth,
            searchViewLayoutParam.topMargin,
            r - searchViewLayoutParam.rightMargin,
            searchViewLayoutParam.topMargin + searchView.measuredHeight
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
    private fun Int.dp(context: Context) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}