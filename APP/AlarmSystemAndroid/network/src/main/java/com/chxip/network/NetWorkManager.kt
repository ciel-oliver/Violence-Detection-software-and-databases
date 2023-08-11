package com.chxip.network

import android.R.attr.bitmap
import android.graphics.Bitmap
import android.util.ArrayMap
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.ref.SoftReference
import java.util.concurrent.TimeUnit


/**
 * @ClassName: NetWorkManager
 * @Description: 网络访问
 */
class NetWorkManager {

    private var retrofit: Retrofit? = null
    private var okhttpClient: OkHttpClient? = null

    fun getOkhttpClient(): OkHttpClient? {
        return okhttpClient
    }


    //网络访问API Service 缓存
    private val apiArrayMaps = ArrayMap<String, SoftReference<Any>>()


    var timeOut = 600000

    companion object{
        private var mInstance: NetWorkManager? = null

        fun getInstance(): NetWorkManager {
            if (mInstance == null) {
                synchronized(NetWorkManager::class.java) {
                    if (mInstance == null) {
                        mInstance = NetWorkManager()
                    }
                }
            }
            return mInstance!!
        }
    }

    /**
     * 初始化必要对象和参数
     */
    fun init() {
        val builder = OkHttpClient.Builder()
        // 初始化okhttp
        builder.connectTimeout(timeOut.toLong(), TimeUnit.MILLISECONDS)
            .readTimeout(timeOut.toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout(timeOut.toLong(), TimeUnit.MILLISECONDS)
        builder.sslSocketFactory(
            SSLSocketClient.getSSLSocketFactory(),
            SSLSocketClient.getX509TrustManager()
        )
        builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier())
        //添加请求头
        builder.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val token = "token"
                val newRequest = chain.request()
                    .newBuilder() //.addHeader("Authorization",SharedTools.getString(SharedTools.Token))
                    .addHeader("token", "IAuth $token")
                    .addHeader("platform", "Android")
                    .build()
                return chain.proceed(newRequest)
            }
        })

        if (BuildConfig.DEBUG) { //发布版本不再打印
            //声明日志类
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            //设定日志级别
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(httpLoggingInterceptor)
        }
        okhttpClient = builder.build()
        // 初始化Retrofit
        retrofit = Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl(Urls.BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    /**
     * 获取网络访问Service
     *
     * @param service
     * @param <T>
     * @return
    </T> */
    fun <T> getServiceRequest(service: Class<T>): T {
        val serviceObject: Any
        if (apiArrayMaps.containsKey(service.name)) {
            serviceObject = apiArrayMaps.get(service.name)?.get()!!
        } else {
            serviceObject = retrofit?.create(service)!!
            val softService = SoftReference<Any>(serviceObject)
            apiArrayMaps.put(service.name, softService)
        }
        return serviceObject as T
    }

}
