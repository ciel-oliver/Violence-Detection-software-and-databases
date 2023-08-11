package com.ping.alarmsystem.application

import android.R
import android.content.Context
import androidx.multidex.MultiDex
import com.chxip.base_lib.application.BaseApplication
import com.chxip.network.NetWorkManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator
import com.tencent.mmkv.MMKV



class MyApplication : BaseApplication() {


    companion object{
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator(DefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.white, com.chxip.base_lib.R.color.text_gray_6) //全局设置主题颜色
                //指定为经典Header
                ClassicsHeader(context).setEnableLastTime(true).setDrawableSize(20f)
            })
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter(context).setDrawableSize(20f)
            }
            ClassicsFooter.REFRESH_FOOTER_NOTHING = "我是有底线的~"
        }

    }





    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        //初始化网络访问
        NetWorkManager.getInstance().init()
        //初始化MMKV
        MMKV.initialize(this)
    }
}