package com.yookiely.lostfond2.search

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Window
import android.support.v7.widget.Toolbar
import android.widget.EditText
import android.widget.ImageView
import com.example.lostfond2.R
import com.orhanobut.hawk.Hawk
import com.twt.wepeiyang.commons.mta.mtaBegin
import com.twt.wepeiyang.commons.mta.mtaClick
import com.twt.wepeiyang.commons.mta.mtaEnd
import com.yookiely.lostfond2.service.Utils


class SearchInitActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var imageView: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter
    private lateinit var imageViewClean: ImageView
    private lateinit var editText: EditText
    private var historyRecordData: MutableList<String> = mutableListOf()
    private val timeOfEdit = "lostfound2_搜索 光标在搜索框内停留的时长"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)// 隐藏actionbar，需在setContentView前面
        setContentView(R.layout.lf2_activity_search_init)
        window.statusBarColor = resources.getColor(R.color.statusBarColor)
        toolbar = findViewById(R.id.tb_search_init)
        imageView = findViewById(R.id.iv_search_init_icon_right)
        recyclerView = findViewById(R.id.rv_search_init_hr_rv)
        imageViewClean = findViewById(R.id.iv_search_init_clean)
        editText = findViewById(R.id.et_search_init_et)
        editText.setHintTextColor(Color.parseColor("#ffffff"))
        // imageViewBack = findViewById(R.id.iv_search_init_back)

        initRV()// 初始化搜索历史的rv

        imageView.setOnClickListener {
            // 点击放大镜，开始搜索
            val query = editText.text.toString()
            if (query != "") {
                // database(query,db)//处理搜索历史的数据库
                // 并把搜索输入的内容传给展示搜索结果的activity
                submit(query)
                val intent = Intent()
                val bundle = Bundle()
                bundle.putString(Utils.QUERY_KEY, query)
                intent.putExtras(bundle)
                intent.setClass(this@SearchInitActivity, SearchActivity::class.java)
                ContextCompat.startActivity(this@SearchInitActivity, intent, bundle)
            }
        }

        imageViewClean.setOnClickListener {
            // 清空搜索记录
            mtaClick("lostfound2_搜索 清除搜索历史的次数",this@SearchInitActivity)
            Hawk.put<MutableList<String>>(Utils.SEARCH_LIST_KEY, mutableListOf())
            getHistoryData()
        }

        toolbar.apply {
            setSupportActionBar(this)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }
        // imageViewBack.setOnClickListener { onBackPressed() }//返回

        editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mtaBegin(timeOfEdit, this@SearchInitActivity)
            } else {
                mtaEnd(timeOfEdit, this@SearchInitActivity)
            }
        }

        recyclerView.setOnClickListener {
            mtaClick("lostfound2_搜索 光标在搜索框内停留的时长",this@SearchInitActivity)
        }
    }

    override fun onRestart() {
        super.onRestart()
        getHistoryData()
        editText.setText(historyRecordData[0])
        // 从搜索结果界面回到搜索界面
    }

    private fun initRV() {
        adapter = SearchAdapter(historyRecordData, this)
        adapter.setHasStableIds(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        getHistoryData()
    }

    private fun submit(query: String) {
        var temp = mutableListOf<String>()
        if (Hawk.get<MutableList<String>>(Utils.SEARCH_LIST_KEY) != null) {
            temp = Hawk.get<MutableList<String>>(Utils.SEARCH_LIST_KEY)
            temp.remove(query)
        }
        temp.add(query)
        if (temp.size > 5) {
            temp.removeAt(0)
        }
        Hawk.put(Utils.SEARCH_LIST_KEY, temp)
    }

    private fun getHistoryData() {
        // 获取搜索历史的数据
        // 只要每次企图更改hawk内存储的数据，就会调用改方法
        if (Hawk.get<MutableList<String>>(Utils.SEARCH_LIST_KEY) != null) {
            historyRecordData.clear()
            historyRecordData.addAll((Hawk.get<MutableList<String>>(Utils.SEARCH_LIST_KEY) as MutableList<String>))
            historyRecordData.reverse()
        }
        adapter.notifyDataSetChanged()
    }
}