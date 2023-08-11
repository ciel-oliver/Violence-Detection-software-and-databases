package com.ping.alarmsystem.api

import com.chxip.network.entity.Response
import com.ping.alarmsystem.entity.*
import retrofit2.http.*

interface AlarmServiceApi {

    //获取最新的报警
    @GET("alarm/api/getAlarm")
    suspend fun getAlarm(@Query("alarmType") alarmType :Int):
            Response<List<Alarm>>

    //接受报警
    @POST("alarm/api/updateAlarmUserId")
    @FormUrlEncoded
    suspend fun updateAlarmUserId( @Field("userId")  userId:Int,
                                   @Field("alarmId")  alarmId:Int):
            Response<String>

    //获取历史的报警
    @GET("alarm/api/getAlarmByUserId")
    suspend fun getAlarmByUserId(@Query("page")  page:Int,
                         @Query("size")  size:Int,
                         @Query("userId")  userId:Int):
            Response<List<Alarm>>

    //接受报警
    @POST("alarm/api/updateAlarmStatus")
    @FormUrlEncoded
    suspend fun updateAlarmStatus( @Field("status")  status:Int,
                                   @Field("alarmId")  alarmId:Int,
                                 @Field("alarmRemark" ) alarmRemark:String):
            Response<String>

    //获取通知
    @GET("notification/api/getNotification")
    suspend fun getNotification(@Query("page")  page:Int,
                                 @Query("size")  size:Int ):
            Response<List<Notification>>


    //获取通知
    @GET("notification/api/getNewNotification")
    suspend fun getNewNotification(@Query("date")  date:String ):
            Response<Int>

}