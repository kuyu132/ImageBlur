package com.kuyuzhiqi.imageblur.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kuyuzhiqi.imageblur.LibraryLoadUtil
import com.kuyuzhiqi.imageblur.R
import kotlinx.android.synthetic.main.activity_gass_blur.*

class GassBlurActivity : AppCompatActivity() {

    private lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gass_blur)

        init()
    }

    private fun init() {
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.bg)
        LibraryLoadUtil().gaussBlur(bitmap)
        iv2.setImageBitmap(bitmap)
    }
}
