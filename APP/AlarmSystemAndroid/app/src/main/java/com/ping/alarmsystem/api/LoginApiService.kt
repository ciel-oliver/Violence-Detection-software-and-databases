package com.ping.alarmsystem.api

import com.chxip.network.Urls
import com.chxip.network.entity.Response
import com.ping.alarmsystem.entity.User
import retrofit2.http.*

/**
 * @Description: 登录 接口
 */
interface LoginApiService {
    //登录
    @POST(Urls.BASE + "user/api/login")
    suspend fun login(@Query("username") username: String,@Query("password") password: String): Response<User>


    //注册
    @POST(Urls.BASE + "user/api/addUser")
    suspend fun register(@Body user:User): Response<User>

    //修改
    @POST(Urls.BASE + "user/api/updateUser")
    suspend fun updateUser(@Body user:User): Response<User>


    //注册
    @FormUrlEncoded
    @POST(Urls.BASE + "user/api/updatePassword")
    suspend fun updatePassword(@Field("newPassword")  newPassword:String,
                               @Field("oldPassword")  oldPassword:String,
                               @Field("userId")  userId:Int ): Response<User>

    //获取所有用户
    @GET(Urls.BASE + "user/api/selectAllByUserId")
    suspend fun selectAllByUserId(@Query("page")  page:Int,
                               @Query("userName")  userName:String,
                               @Query("userId")  userId:Int ): Response<List<User>>
}