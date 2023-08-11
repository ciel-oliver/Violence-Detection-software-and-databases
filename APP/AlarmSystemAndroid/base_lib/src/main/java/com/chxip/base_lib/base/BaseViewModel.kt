package com.chxip.base_lib.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @ClassName: BaseViewModel
 * @Description: BaseViewMOdel
 * @Author: chxip
 * @CreateDate: 4/20/21 10:12 AM
 */
open class BaseViewModel(application: Application) :AndroidViewModel(Application()), DefaultLifecycleObserver {
    //contetx
    val context:Context = getApplication()

    //协程
    val mainScope = MainScope()


    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        //清除协程任务
        mainScope.cancel()
    }

}