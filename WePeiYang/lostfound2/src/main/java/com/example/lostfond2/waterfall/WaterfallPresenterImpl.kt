package com.example.lostfond2.waterfall

import android.util.Log
import com.example.lostfond2.service.LostFoundService
import com.example.lostfond2.service.MyListDataOrSearchBean
import com.orhanobut.hawk.Hawk
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.network.RetrofitProvider
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class WaterfallPresenterImpl(var waterfallView: WaterfallContract.WaterfallView)
    : WaterfallContract.WaterfallPresenter {


    override fun setWaterfallData(waterfallBean: List<MyListDataOrSearchBean>) {
        waterfallView.setWaterfallData(waterfallBean)
    }

    override fun loadWaterfallData(lostOrFound: String, page: Int) {


//        async(UI + QuietCoroutineExceptionHandler) {
//            val dataList = when (lostOrFound) {
//                "lost" -> lostFoundService.getLost(page, 0).awaitAndHandle { it.printStackTrace() }?.data!!
//                else -> lostFoundService.getFound(page, 0).awaitAndHandle { it.printStackTrace() }?.data!!
//            }
//
//            setWaterfallData(dataList)
//        }
        if (Hawk.contains("campus")) {
            val campus: Int = Hawk.get("campus")
            Log.d("momom", campus.toString())
            launch(UI + QuietCoroutineExceptionHandler) {
                val dataList = when (lostOrFound) {
                    "lost" -> LostFoundService.getLost(campus, page, 0).await()
                    else -> LostFoundService.getFound(campus, page, 0).await()
                }

                if (dataList.error_code == -1) {
                    setWaterfallData(dataList.data!!)
                }
            }
        }
    }

    override fun loadWaterfallDataWithType(lostOrFound: String, page: Int, type: Int) {
        if (type == -1) {
            loadWaterfallData(lostOrFound, page)
        } else {
//            async(UI + QuietCoroutineExceptionHandler) {
//                val dataList = when (lostOrFound) {
//                    "lost" -> lostFoundService.getLost(page, type).awaitAndHandle { it.printStackTrace() }?.data!!
//                    else -> lostFoundService.getFound(page, type).awaitAndHandle { it.printStackTrace() }?.data!!
//                }
//
//                setWaterfallData(dataList)
//            }
            if (Hawk.contains("campus")) {
                val campus: Int = Hawk.get("campus")
                launch(UI + QuietCoroutineExceptionHandler) {
                    val dataList = when (lostOrFound) {
                        "lost" -> LostFoundService.getLost(campus, page, type).await()
                        else -> LostFoundService.getFound(campus, page, type).await()
                    }

                    if (dataList.error_code == -1) {
                        setWaterfallData(dataList.data!!)
                    }
                }
            }
        }
    }
}