package com.chxip.network.entity

/**
 * @ClassName: WeatherResponse
 * @Description: java类作用描述
 * @Author: chxip
 * @CreateDate: 2022/3/19 15:59
 */
data class WeatherResponse<T>(
    //errorCode = 0 代表执行成功，不建议依赖任何非0的 errorCode.
    //errorCode = -1001 代表登录失效，需要重新登录。
    val code: Int, //10000	更多返回参数示例值请参看“错误参照码”

    val msg: String, //查询成功	更多返回参数示例值请参看“错误参照码”

    val charge: Boolean, //	false 或 true	false：不扣费 true：扣费

    val result: T
)