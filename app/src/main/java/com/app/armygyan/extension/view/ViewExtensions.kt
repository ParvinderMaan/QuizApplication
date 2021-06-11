package com.app.armygyan.extension.view

import android.view.View
import com.app.armygyan.R
import com.app.armygyan.extension.context.getColorRes
import com.app.armygyan.extension.context.getColorRes

import com.google.android.material.snackbar.Snackbar

/**
 * View's visibility extensions.
 */

fun View.visible() {
    changeViewVisibility(View.VISIBLE)
}

fun View.gone() {
    changeViewVisibility(View.GONE)
}

fun View.invisible() {
    changeViewVisibility(View.INVISIBLE)
}

private fun View.changeViewVisibility(newState: Int) {
    visibility = newState
}

/**
 * Snackbar's extensions.
 */

fun View.showSmallSnackbar(text: String) {
    showSnackbar(text, Snackbar.LENGTH_SHORT)
}

fun View.showLongSnackbar(text: String) {
    showSnackbar(text, Snackbar.LENGTH_LONG)
}

fun View.showIndefiniteSnackbar(text: String) {
    showSnackbar(text, Snackbar.LENGTH_INDEFINITE)
}

private fun View.showSnackbar(text: String, duration: Int) {
    val snackBar=Snackbar.make(this, text, duration)
    snackBar.view.setBackgroundColor(this.context.getColorRes(R.color.colorGreen))
    snackBar.show()
}


