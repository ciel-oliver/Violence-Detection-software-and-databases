package com.chxip.network

import com.chxip.base_lib.util.ToastUtil
import com.chxip.network.entity.ErrorResult
import com.chxip.network.entity.Response
import com.chxip.network.entity.WeatherResponse
import kotlinx.coroutines.*


/**
 * @ClassName: NetWork
 * @Description: 网络访问
 */
object NetWork {

    val viewModelScope = GlobalScope

    /**
     * 注意此方法传入的参数：api是以函数作为参数传入
     * api：即接口调用方法
     * error：可以理解为接口请求失败回调
     * ->数据类型，表示方法返回该数据类型
     * ->Unit，表示方法不返回数据类型
     */
    fun <T> launch(
        api: suspend CoroutineScope.() -> Response<T>,
        complete: suspend (T) -> Unit, myError:
        suspend (ErrorResult) -> Unit, scope: CoroutineScope? = null,
        isShowLoading: Boolean = false, isShowToast: Boolean = true
    ) { //显示dialog
        if (isShowLoading) {

        }
        if (scope != null) {
            scope.launch {
                try {
                    withContext(Dispatchers.IO) { //异步请求接口
                        val result = api()
                        withContext(Dispatchers.Main) {
                            if (result.errorId == 0) { //请求成功
                                complete(result.data)
                            } else {
                                if (isShowToast) {
                                    ToastUtil.show(result.errorMsg)
                                }
                                myError(ErrorResult(result.errorId, result.errorMsg))
                            }
                        }
                    }
                } catch (e: Throwable) { //接口请求失败
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        val error = HttpErrorUtil.getError(e)
                        if (isShowToast) {
                            ToastUtil.show(error.errMsg)
                        }
                        myError(error)
                    }
                } finally { //请求结束
                    //关闭dialog
                }
            }
        } else {
            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) { //异步请求接口
                        val result = api()
                        withContext(Dispatchers.Main) {
                            if (result.errorId == 0) { //请求成功
                                complete(result.data)
                            } else {
                                if (isShowToast) {
                                    ToastUtil.show(result.errorMsg)
                                }
                                myError(ErrorResult(result.errorId, result.errorMsg))
                            }
                        }
                    }
                } catch (e: Throwable) { //接口请求失败
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        val error = HttpErrorUtil.getError(e)
                        if (isShowToast) {
                            ToastUtil.show(error.errMsg)
                        }
                        myError(error)
                    }
                } finally { //请求结束
                    //关闭dialog
                }
            }
        }

    }


    /**
     * 注意此方法传入的参数：api是以函数作为参数传入
     * api：即接口调用方法
     * error：可以理解为接口请求失败回调
     * ->数据类型，表示方法返回该数据类型
     * ->Unit，表示方法不返回数据类型
     */
    fun <T> launchWeather(

        api: suspend CoroutineScope.() -> WeatherResponse<T>,
        complete: suspend (T) -> Unit,
        myError: suspend (ErrorResult) -> Unit,
        scope: CoroutineScope? = null,
        isShowLoading: Boolean = false, isShowToast: Boolean = true
    ) { //显示dialog
        if (isShowLoading) {
        }
        if (scope != null) {
            scope.launch {
                try {
                    withContext(Dispatchers.IO) { //异步请求接口
                        val result = api()
                        withContext(Dispatchers.Main) {
                            if (result.code == 10000) { //请求成功
                                complete(result.result)
                            } else {
                                if (isShowToast) {
                                    ToastUtil.show(result.msg)
                                }
                                myError(ErrorResult(result.code, result.msg))
                            }
                        }
                    }
                } catch (e: Throwable) { //接口请求失败
                    withContext(Dispatchers.Main) {
                        val error = HttpErrorUtil.getError(e)
                        if (isShowToast) {
                            ToastUtil.show(error.errMsg)
                        }
                        myError(error)
                    }
                } finally { //请求结束
                    //关闭dialog
                }
            }
        } else {
            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) { //异步请求接口
                        val result = api()
                        withContext(Dispatchers.Main) {
                            if (result.code == 10000) { //请求成功
                                complete(result.result)
                            } else {
                                if (isShowToast) {
                                    ToastUtil.show(result.msg)
                                }
                                myError(ErrorResult(result.code, result.msg))
                            }
                        }
                    }
                } catch (e: Throwable) { //接口请求失败
                    withContext(Dispatchers.Main) {
                        val error = HttpErrorUtil.getError(e)
                        if (isShowToast) {
                            ToastUtil.show(error.errMsg)
                        }
                        myError(error)
                    }
                } finally { //请求结束
                    //关闭dialog
                }
            }
        }
    }

    /**
     * 清除
     */
    fun cancel() {
        viewModelScope.cancel()
    }
}