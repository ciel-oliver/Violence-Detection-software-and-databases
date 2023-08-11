package com.zhouzun.base_lib.base

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.chxip.base_lib.base.BaseViewModel
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * Created by chxip on 2019/8/21.
 */
abstract class MVBaseFragment<T : ViewBinding, VM : BaseViewModel> : Fragment() {
    val viewBinding: T get() = _viewBinding!!
    private var _viewBinding: T? = null

    //ViewModel
    lateinit var viewModel: VM

    lateinit var mView: View

    //是否自动显示dialog 设置为false，需要子页面自己处理
    var isDefaultShowDialog = true


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
        //ViewModel
        provideViewModel()
        //lifecycle绑定
        lifecycle.addObserver(viewModel)
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
        isFirstLoad=true
        _viewBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mContext = null
    }

    /**
     * 获取ViewModel
     */
    private fun provideViewModel() {
        val clazz: Class<VM> = getViewModelClass(javaClass)
        val application: Application = requireActivity().application
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(clazz)

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

}