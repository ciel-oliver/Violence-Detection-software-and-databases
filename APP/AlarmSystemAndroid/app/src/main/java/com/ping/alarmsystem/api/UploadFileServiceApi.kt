package com.ping.wegame.api

import com.chxip.network.entity.Response
import okhttp3.MultipartBody
import retrofit2.http.*

interface UploadFileServiceApi {
    //上传图片
    @Multipart
    @POST("upload/api/uploadImage")
    suspend fun uploadImage(@Part file: MultipartBody.Part  ):
            Response<String>
}