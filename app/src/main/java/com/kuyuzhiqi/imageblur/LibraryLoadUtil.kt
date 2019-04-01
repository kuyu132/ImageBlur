package com.kuyuzhiqi.imageblur

import android.graphics.Bitmap
import android.os.AsyncTask

class LibraryLoadUtil {
    init {
        System.loadLibrary("native-lib")
        System.loadLibrary("native-lib2")
    }

    external fun stringFromJNI(): String

    external fun stringFromJNI2(): String

    external fun gaussBlur(bitmap: Bitmap)

    external fun imgToGray(buf: IntArray, w: Int, h: Int): IntArray

    public fun createBitmap(pixels: IntArray, w: Int, h: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        return bitmap
    }
}
