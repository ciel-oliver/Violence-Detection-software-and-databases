package com.zhouzun.base_lib.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * @ClassName: UIUtil
 * @Description: java类作用描述
 * @Author: chxip
 * @CreateDate: 2021/7/1 14:12
 */
object UIUtil {

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    fun dipToPx(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     *获取屏幕尺寸的宽度PX
     * @param context
     */
    fun getScreenWidth(context: Context):Int{
        val displayMetrics = DisplayMetrics()
        val wm:WindowManager  = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels
    }


    /**
     *获取屏幕尺寸的高度PX
     * @param context
     */
    fun getScreenHeight(context: Context):Int{
        val displayMetrics = DisplayMetrics()
        val wm:WindowManager  = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels
    }


}