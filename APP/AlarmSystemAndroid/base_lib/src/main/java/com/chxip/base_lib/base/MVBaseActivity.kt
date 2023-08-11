package com.chxip.base_lib.base

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.chxip.base_lib.base.BaseViewModel
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

abstract class MVBaseActivity<T : ViewBinding, VM : BaseViewModel> :
    FragmentActivity(), View.OnClickListener {

    //DataBinding
    lateinit var viewBinding: T

    //ViewModel
    lateinit var viewModel: VM


    override fun setContentView(layoutId: Int) {
        try {
            val vmClass =
                (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
            val method: Method = vmClass.getMethod("inflate", LayoutInflater::class.java)
            viewBinding = method.invoke(null, layoutInflater) as T
            setContentView(viewBinding.root)
        } catch (e: NoSuchMethodException) {
            //e.printStackTrace()
        } catch (e: IllegalAccessException) {
            //e.printStackTrace()
        } catch (e: InvocationTargetException) {
           // e.printStackTrace()
        }catch (e: Exception) {
            // e.printStackTrace()
        }
        //防止软件键盘把布局顶上去
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)



        //ViewModel
        provideViewModel()

        //lifecycle绑定
        lifecycle.addObserver(viewModel)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //禁止横屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        context = this
    }

    /**
     * 获取ViewModel
     */
    private fun provideViewModel() {
        val clazz: Class<VM> = getViewModelClass(javaClass)
        val application: Application = this.application
        viewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(clazz)
    }

    /**
     * 获取VMclass
     */
    private fun getViewModelClass(aClass: Class<*>): Class<VM> {
        val type = aClass.genericSuperclass

        return if (type is ParameterizedType) {
            type.actualTypeArguments[1] as Class<VM>
        } else {
            getViewModelClass(aClass.superclass)
        }
    }

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
     * 左边按钮的点击事件，默认是关闭Activity
     */
    protected fun onLeftClick() {
        finish()
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
        val imm =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(this.getWindow().getDecorView().getWindowToken(), 0)
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