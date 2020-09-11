package com.app.armygyan.base

import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.app.armygyan.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {

    fun showSnackBar(msg: String, @ColorRes colorId: Int = R.color.colorRed) {
        val snackBar = Snackbar.make(getRootView(), msg, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(ContextCompat.getColor(this, colorId))
        snackBar.show()
    }

    abstract fun getRootView(): View

}