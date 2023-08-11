package com.chxip.network.entity

/**
 * @ClassName: Response
 * @Description: 接口数据返回基本格式
 * @Author: chxip
 * @CreateDate: 4/20/21 9:59 AM
 */
data class Response<T>(
    //errorCode = 0 代表执行成功，不建议依赖任何非0的 errorCode.
    //errorCode = -1001 代表登录失效，需要重新登录。
    val errorId: Int,

    val errorMsg: String,

    val data: T
)
