package com.app.armygyan.extension.context

import android.content.Context
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.app.armygyan.R

/**
 * Toast extensions.
 */

fun Context.showToast(text:String) {
    Toast.makeText(this, text,  Toast.LENGTH_SHORT).show()
}

 fun Context.getColorRes(@ColorRes colorId:Int): Int {
    return ContextCompat.getColor(this, colorId)
}