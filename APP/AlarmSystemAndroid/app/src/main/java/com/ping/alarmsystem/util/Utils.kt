package com.ping.alarmsystem.util

import java.lang.Exception

/**
 * @ClassName: Utils
 * @Description: java类作用描述
 */
object Utils {
    /**
     * 字符串转Int
     *
     * @param data
     * @return
     */
    fun getInt(data: String): Int {
        var i = -1
        try {
            i = data.toInt()
        } catch (e: Exception) {
        }
        return i
    }
}