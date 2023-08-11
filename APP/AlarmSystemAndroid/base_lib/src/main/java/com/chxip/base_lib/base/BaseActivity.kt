package com.zhouzun.base_lib.base

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.chxip.base_lib.R
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class BaseActivity<T : ViewBinding> :
    FragmentActivity(), View.OnClickListener {

    //viewBinding
    lateinit var viewBinding: T

    lateinit var view:View


    override fun setContentView(layoutId: Int) {
        try {
            val vmClass =
                (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
            val method: Method = vmClass.getMethod("inflate", LayoutInflater::class.java)
            viewBinding = method.invoke(null, layoutInflater) as T
            view = viewBinding.root
            setContentView(view)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        //防止软件键盘把布局顶上去
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //禁止横屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        context = this


    }

    var tv_title:TextView? = null
    var iv_left:ImageView? = null

    fun setTitle(title:String){
        if(tv_title == null ){
            tv_title = view.findViewById(R.id.tv_title)
            iv_left = view.findViewById(R.id.iv_left)
            iv_left?.setOnClickListener{
                finish()
            }
        }
        tv_title?.text = title
    }

   /* fun setTitle(titleId:Int){
        tv_title?.text = getString(titleId)
    }*/

    //上下文对象
    var context: Context? = null
    override fun onStart() {
        super.onStart()
    }

    /**
     * 初始化页面
     *
     * @return
     */
    protected abstract fun initViews()


    override fun onClick(view: View) {

    }

    /**
     * 获取颜色
     */
    protected fun getColors(colorId: Int): Int {
        return resources.getColor(colorId)
    }


    /**
     * 跳转Activity
     */
    fun toActivity(c: Class<*>?) {
        val intent = Intent(this, c)
        startActivity(intent)
    }

    /**
     * 跳转Activity
     */
    fun toActivity(intent: Intent?) {
        startActivity(intent)
    }

    public override fun onResume() {
        super.onResume()
        Glide.with(this).resumeRequests()
    }

    public override fun onPause() {
        super.onPause()
        Glide.with(this).pauseRequests()
    }

    override fun onDestroy() {
        context = null
        super.onDestroy()
    }

    /**
     * 关闭软键
     */
    fun closeKeyboard() {
        if (this.currentFocus != null) {
            val mInputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
        }
    }

    // 点击空白区域 自动隐藏软键盘
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (null != this.currentFocus) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            closeKeyboard()
        }
        return super.onTouchEvent(event)
    }



}