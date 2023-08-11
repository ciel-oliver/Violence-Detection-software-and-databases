package com.zhouzun.base_lib.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * Created by chxip on 2019/8/21.
 */
abstract class BaseFragment<T : ViewBinding>  : Fragment() {
    val viewBinding: T get() = _viewBinding!!
    private var _viewBinding: T? = null

    lateinit var mView: View

    var isFirstLoad = true // 是否第一次加载


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = activity


        try {
            val vmClass = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
            val method: Method = vmClass.getMethod("inflate", LayoutInflater::class.java)
            _viewBinding = method.invoke(null, layoutInflater) as T
            mView = _viewBinding!!.root
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return mView
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            // 将数据加载逻辑放到onResume()方法中
            initData()
            isFirstLoad = false;
        }
    }
    /**
     * 初始加载数据
     * @return
     */
    protected abstract fun initData()


    /**
     * 返回fragment显示的View的ID
     * @return
     */
    protected abstract fun getLayoutId(): Int

    //上下文对象
    var mContext: Context? = null

    /**
     * 跳转Activity
     */
    fun toActivity(c: Class<*>?) {
        val intent = Intent(activity, c)
        startActivity(intent)
    }

    /**
     * 跳转Activity
     */
    fun toActivity(intent: Intent?) {
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isFirstLoad = true
        _viewBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mContext = null
    }



}