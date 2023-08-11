package com.ping.alarmsystem.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chxip.base_lib.base.BaseAdapter
import com.chxip.network.Urls
import com.makeramen.roundedimageview.RoundedImageView
import com.ping.alarmsystem.R
import com.ping.alarmsystem.entity.Alarm


class AlarmNewAdapter(dataList: MutableList<Alarm>, context: Context) :
    BaseAdapter(dataList, context) {

    var type = 1
    var useId = 0

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val view: View = layoutInflater.inflate(R.layout.alarm_new_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val holder = viewHolder as ViewHolder
        val data: Alarm = dataList.get(position) as Alarm
        val base64 = data.alarmImage
       val decodedString = Base64.decode(base64, Base64.DEFAULT);
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size);
        holder.iv_alarm_img.setImageBitmap(decodedByte);
        holder.tv_alarm_address.text =data.alarmAddress
        holder.tv_alarm_time.text = data.alarmTime
        holder.tv_alarm_dispose.setOnClickListener {
            //item 点击事件
            onItemClickListener?.onClickListener(it, position)
        }
        if(type == 2){
            holder.tv_alarm_dispose.text = "完成处理"
        }else{
            holder.tv_alarm_dispose.text = "去处理"
        }
        if(data.alarmStatus == 3){
            holder.tv_alarm_dispose.visibility = View.GONE
        }else{
            if(data.alarmStatus == 2){
                if(data.userId == useId) {
                    holder.tv_alarm_dispose.visibility = View.VISIBLE
                }else{
                    holder.tv_alarm_dispose.visibility = View.GONE
                }
            }else{
                holder.tv_alarm_dispose.visibility = View.VISIBLE
            }
        }
        if(data.completeTime.isNullOrEmpty()){
            holder.tv_alarm_clsj.visibility = View.GONE
        }else{
            holder.tv_alarm_clsj.visibility = View.VISIBLE
            holder.tv_alarm_clsj.text = "处理时间："+data.completeTime
        }
        if(data.alarmRemark.isNullOrEmpty()){
            holder.tv_alarm_clfs.visibility = View.GONE
        }else{
            holder.tv_alarm_clfs.visibility = View.VISIBLE
            holder.tv_alarm_clfs.text = "处理方式："+data.alarmRemark
        }
        if(data.userName.isNullOrEmpty()){
            holder.tv_alarm_user.visibility = View.GONE
        }else{
            holder.tv_alarm_user.visibility = View.VISIBLE
            holder.tv_alarm_user.text = "处理人："+data.userName
        }

    }



    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_alarm_address: TextView
        var tv_alarm_time: TextView
        var iv_alarm_img: ImageView
        var tv_alarm_dispose : TextView
        var tv_alarm_clsj : TextView
        var tv_alarm_clfs : TextView
        var tv_alarm_user:TextView
        init {
            tv_alarm_address = itemView.findViewById(R.id.tv_alarm_address)
            tv_alarm_time = itemView.findViewById(R.id.tv_alarm_time)
            iv_alarm_img = itemView.findViewById(R.id.iv_alarm_img)
            tv_alarm_dispose = itemView.findViewById(R.id.tv_alarm_dispose)
            tv_alarm_clsj = itemView.findViewById(R.id.tv_alarm_clsj)
            tv_alarm_clfs = itemView.findViewById(R.id.tv_alarm_clfs)
            tv_alarm_user = itemView.findViewById(R.id.tv_alarm_user)
        }
    }


}