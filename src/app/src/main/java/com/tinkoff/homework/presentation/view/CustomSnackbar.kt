package com.tinkoff.homework.presentation.view

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.homework.R


object CustomSnackbar {
    fun makeLongText(view : View, text: CharSequence): Snackbar {
        val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
        snackbar.setTextColor(view.context.getColor(R.color.secondary_text))
        snackbar.setBackgroundTint(view.context.getColor(R.color.custom_background_secondary))
        return snackbar
    }
}