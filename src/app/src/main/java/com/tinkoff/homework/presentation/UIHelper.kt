package com.tinkoff.homework.presentation

import android.content.Context
import android.util.TypedValue


fun Int.dp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    context.resources.displayMetrics
).toInt()