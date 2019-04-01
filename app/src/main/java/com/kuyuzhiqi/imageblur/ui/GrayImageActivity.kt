package com.kuyuzhiqi.imageblur.ui

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kuyuzhiqi.imageblur.LibraryLoadUtil
import com.kuyuzhiqi.imageblur.R
import kotlinx.android.synthetic.main.activity_gray_image.*

class GrayImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gray_image)

        initViews()
    }

    private fun initViews() {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.bg)
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val convertPixels: IntArray = LibraryLoadUtil().imgToGray(pixels, width, height)
        LibraryLoadUtil().createBitmap(convertPixels, width, height).apply {
            iv_gray.setImageBitmap(this)
        }

    }
}
