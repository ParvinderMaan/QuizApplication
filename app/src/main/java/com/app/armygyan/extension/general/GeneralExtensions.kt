package com.app.armygyan.extension.general

import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.core.app.ActivityCompat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.abs

/**
 * General extensions for ArrayList.
 */
fun <T: Any> ArrayList<T>.replaceItemInList(item: T): ArrayList<T> {
    if(this.contains(item)) this.remove(item) else this.add(item)
    return this
}

fun <T: Any> ArrayList<T>.refreshList(items: List<T>): ArrayList<T> {
    this.clear()
    this.addAll(items)
    return this
}

fun <T: Any> ArrayList<T>.addOnlyNewItemInList(items: List<T>): ArrayList<T> {
    items.forEach { if(!this.contains(it)) this.add(it)  }
    return this
}

/**
 * General extensions for MutableList.
 */

fun <T: Any> MutableList<T>.replaceItemInList(item: T): MutableList<T> {
    if(this.contains(item)) this.remove(item) else this.add(item)
    return this
}

fun <T: Any> MutableList<T>.refreshList(items: List<T>): MutableList<T> {
    this.clear()
    this.addAll(items)
    return this
}

fun <T: Any> MutableList<T>.addOnlyNewItemInList(items: List<T>): MutableList<T> {
    items.forEach { if(!this.contains(it)) this.add(it)  }
    return this
}

/**
 * General extensions for an Activity.
 */

fun Activity.hasPermission(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

/**
 * General extensions for an Screen conversion.
 */
//means pxToDp
val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
//means dpToPx
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
/**
 * General extensions for an String.
 */
fun String.capitalize(): String {
    return this.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT)
}
