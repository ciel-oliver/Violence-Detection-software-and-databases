package com.chxip.base_lib.util

import com.google.gson.Gson

/**
 * @ClassName: Gson
 * @Description: java类作用描述
 * @Author: chxip
 * @CreateDate: 2021/6/9 17:08
 */
object GsonUtil {
    lateinit var GSON:Gson

    init {
        GSON = Gson()
    }


}