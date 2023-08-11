package com.chxip.network

import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

/**
 * @ClassName: SSLSocketClient
 * @Description: java类作用描述
 */
class SSLSocketClient {
    companion object{
        //获取这个SSLSocketFactory
        fun getSSLSocketFactory(): SSLSocketFactory {
            return try {
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, getTrustManager(), SecureRandom())
                sslContext.socketFactory
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }

        //获取TrustManager
        private fun getTrustManager(): Array<TrustManager>? {
            return arrayOf(object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })
        }

        //获取HostnameVerifier
        fun getHostnameVerifier(): HostnameVerifier {
            return HostnameVerifier { s, sslSession -> true }
        }

        fun  getX509TrustManager():X509TrustManager{
            val trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm()
            )
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                ("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers))
            }
            val trustManager = trustManagers[0] as X509TrustManager

            return  trustManager
        }

    }
}