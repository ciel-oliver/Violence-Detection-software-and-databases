package com.ping.alarmsystem.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chxip.base_lib.base.RecycleViewDivider
import com.chxip.base_lib.constant.Constant
import com.chxip.base_lib.util.DateUtils
import com.chxip.base_lib.util.MMKVUtil
import com.chxip.base_lib.util.statusbar.StatusBarUtil
import com.chxip.network.NetWork
import com.chxip.network.NetWorkManager
import com.ping.alarmsystem.R
import com.ping.alarmsystem.adapter.NotificationAdapter
import com.ping.alarmsystem.api.AlarmServiceApi
import com.ping.alarmsystem.databinding.ActivityNotificationBinding
import com.ping.alarmsystem.entity.Notification
import com.ping.alarmsystem.entity.User
import com.zhouzun.base_lib.base.BaseActivity

class NotificationActivity : BaseActivity<ActivityNotificationBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        setTitle("通知")
        initViews()
    }

    var page = 1;
    val notificationServiceApi =
        NetWorkManager.getInstance().getServiceRequest(AlarmServiceApi::class.java)
    var notificationList : MutableList<Notification>? = null
    lateinit var notificationAdapter: NotificationAdapter
    

    override fun initViews() {
        val  user = MMKVUtil.getObject(Constant.USER, User::class.java)
        user?.let {
            //1科室 2站岗位 3志愿者
            if(it.type == 1){
                StatusBarUtil.setStatusBarColor(this,R.color.title_color)
                viewBinding.rlNotificationTitle.setBackgroundResource(R.color.title_color)
            }else if(it.type == 2){
                viewBinding.rlNotificationTitle.setBackgroundResource(R.color.orange)
                StatusBarUtil.setStatusBarColor(this,R.color.orange)
            }else {
                viewBinding.rlNotificationTitle.setBackgroundResource(R.color.red)
                StatusBarUtil.setStatusBarColor(this,R.color.red)
            }
        }


        notificationList = mutableListOf();
        notificationAdapter = NotificationAdapter(notificationList!!,this)
        viewBinding.rvNotification.let {
            val linearLayoutManager = LinearLayoutManager(this)
            it.layoutManager = linearLayoutManager
            it.addItemDecoration( RecycleViewDivider(this, LinearLayoutManager.VERTICAL) )
            it.adapter = notificationAdapter
        }

        notificationAdapter.setOnItemClickListener { view, position ->

        }
        viewBinding.srlNotification.setOnRefreshListener {
            //刷新
            page = 1
            getNotificationAll()
        }
        viewBinding.srlNotification.setOnLoadMoreListener {
            //加载更多
            page++
            getNotificationAll()

        }

        getNotificationAll()

    }


    /**
     * 获取设备列表
     */
    fun getNotificationAll() {
        NetWork.launch({ notificationServiceApi.getNotification(page,20) }, {
            viewBinding.srlNotification.finishRefresh()//结束刷新
            viewBinding.srlNotification.finishLoadMore()//结束加载
            if(it.size < 20){
                viewBinding.srlNotification.setNoMoreData(true)
            }
            if(page == 1){
                notificationList!!.clear()
            }
            notificationList!!.addAll(it)
            notificationAdapter.notifyDataSetChanged()
            if(it.isNotEmpty()){
                MMKVUtil.save("Notification_DATE",it.get(0).createTime)
            }else{
                MMKVUtil.save("Notification_DATE",DateUtils.getCurrentTime())
            }

        }, {
            viewBinding.srlNotification.finishRefresh()//结束刷新
            viewBinding.srlNotification.finishLoadMore()//结束加载
        })

    }
}