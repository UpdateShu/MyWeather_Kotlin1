package com.geekbrains.myweather_kotlin1.model

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
    text: String,
    actionText : String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

fun View.showSnackBar(
    textResId: Int,
    length: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(this, textResId, length).show()
}

fun Context.showLoading(
    textResId: Int
) {
    Toast.makeText(this, textResId, Toast.LENGTH_SHORT).show()
}
