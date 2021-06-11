package com.app.armygyan.helper

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object PaintUtil {

    fun createBitmapOf(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun saveImageOf(bmp: Bitmap,file: File): File {
        try {
            val fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            bmp.recycle()
        } catch (e: IOException) {
            e.printStackTrace()
            bmp.recycle()
        }
        return file
    }

}