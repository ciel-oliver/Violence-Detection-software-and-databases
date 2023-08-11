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
import com.ping.alarmsystem.entity.Notification

class NotificationAdapter(dataList: MutableList<Notification>, context: Context) :
    BaseAdapter(dataList, context) {

    var type = 1

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val view: View = layoutInflater.inflate(R.layout.notification_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val holder = viewHolder as ViewHolder
        val data: Notification = dataList.get(position) as Notification

        holder.tv_notification_title.text =data.notificationTitle
        holder.tv_notification_content.text = data.notificationContent


    }



    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_notification_title: TextView
        var tv_notification_content: TextView
        init {
            tv_notification_title = itemView.findViewById(R.id.tv_notification_title)
            tv_notification_content = itemView.findViewById(R.id.tv_notification_content)
        }
    }


}