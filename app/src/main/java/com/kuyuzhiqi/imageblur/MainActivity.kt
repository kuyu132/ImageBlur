package com.kuyuzhiqi.imageblur

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.kuyuzhiqi.imageblur.ui.BinaryzationActivity
import com.kuyuzhiqi.imageblur.ui.GassBlurActivity
import com.kuyuzhiqi.imageblur.ui.GrayImageActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var list = ArrayList<String>().apply {
            add("高斯模糊")
            add("灰度图像")
            add("二值化")
        }
        rv_list.adapter = ItemAdapter(list)
        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                when (position) {
                    0 -> {
                        startActivity(Intent(this@MainActivity, GassBlurActivity::class.java))
                    }
                    1 -> {
                        startActivity(Intent(this@MainActivity, GrayImageActivity::class.java))
                    }
                    2 -> {
                        startActivity(Intent(this@MainActivity, BinaryzationActivity::class.java))
                    }
                }
            }
        })
    }


    class ItemAdapter(list: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_main, list) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.tv_name, item)
        }
    }
}
