package com.kuyuzhiqi.imageblur

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI() + stringFromJNI2()
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.bg)
        Log.i("tag", "START:" + System.currentTimeMillis().toString())
        ImageBulrAyncTask().execute()
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun stringFromJNI2(): String

    external fun gaussBlur(bitmap: Bitmap)

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
            System.loadLibrary("native-lib2")
        }
    }

    inner class ImageBulrAyncTask : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            gaussBlur(bitmap)
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            iv2.setImageBitmap(bitmap)
            Log.i("tag", "END:" + System.currentTimeMillis().toString())
        }
    }
}
