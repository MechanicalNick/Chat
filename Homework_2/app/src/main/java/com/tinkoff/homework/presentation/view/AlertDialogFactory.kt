package com.tinkoff.homework.presentation.view

import android.app.AlertDialog
import android.view.View
import com.tinkoff.homework.R

class AlertDialogFactory {
    fun create(
        root: View,
        action: () -> Unit
    ): AlertDialog {
        val positiveText = root.resources.getString(R.string.ok)
        val negativeText = root.resources.getString(R.string.cancel)
        val alertDialogBuilderUserInput = AlertDialog.Builder(
            root.context,
            R.style.MyAlertDialogTheme
        )

        alertDialogBuilderUserInput.setView(root)

        alertDialogBuilderUserInput
            .setCancelable(false)
            .setPositiveButton(positiveText) { _, _ ->
                action()
            }
            .setNegativeButton(negativeText) { dialogBox, _ ->
                dialogBox.cancel()
            }

        return alertDialogBuilderUserInput.create()
    }
}