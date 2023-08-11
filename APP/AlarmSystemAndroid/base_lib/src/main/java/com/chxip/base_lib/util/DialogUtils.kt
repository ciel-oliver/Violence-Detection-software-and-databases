package com.chxip.base_lib.util

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chxip.base_lib.R

/**
 * @ClassName: DialogUtils
 * @Description: java类作用描述
 * @Author: ping
 * @CreateDate: 2022/3/22 2:41 下午
 */
object DialogUtils {
    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    fun createLoadingDialog(context: Context, msg: String = "加载中..."): Dialog {
        val inflater = LayoutInflater.from(context)
        val v: View = inflater.inflate(R.layout.loading_dialog, null) // 得到加载view
        val layout = v.findViewById<View>(R.id.dialog_view) as LinearLayout // 加载布局
        // main.xml中的ImageView
        val spaceshipImage = v.findViewById<View>(R.id.img) as ImageView
        val tipTextView = v.findViewById<View>(R.id.tipTextView) as TextView // 提示文字
        // 加载动画
        val hyperspaceJumpAnimation =
            AnimationUtils.loadAnimation(context, R.anim.loading_animation)
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation)
        tipTextView.text = msg // 设置加载信息
        val loadingDialog = Dialog(context, R.style.loading_dialog) // 创建自定义样式dialog
        loadingDialog.setCancelable(true) // 不可以用“返回键”取消
        loadingDialog.setCanceledOnTouchOutside(false) // 点击屏幕外不可取消
        loadingDialog.setContentView(
            layout, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT
            )
        ) // 设置布局
        return loadingDialog
    }
}