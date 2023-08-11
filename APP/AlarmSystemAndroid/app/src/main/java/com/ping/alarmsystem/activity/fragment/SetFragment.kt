package com.ping.alarmsystem.activity.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chxip.base_lib.constant.Constant
import com.chxip.base_lib.util.MMKVUtil
import com.chxip.network.NetWork
import com.chxip.network.NetWorkManager
import com.chxip.network.Urls
import com.ping.alarmsystem.R
import com.ping.alarmsystem.activity.NotificationActivity
import com.ping.alarmsystem.activity.login.LoginActivity
import com.ping.alarmsystem.activity.my.UpdatePasswordActivity
import com.ping.alarmsystem.api.AlarmServiceApi
import com.ping.alarmsystem.databinding.MyFragmentBinding
import com.ping.alarmsystem.databinding.SetFragmentBinding
import com.ping.alarmsystem.entity.User
import com.zhouzun.base_lib.base.BaseFragment

class SetFragment: BaseFragment<SetFragmentBinding>()  {
    override fun getLayoutId(): Int {
        return R.layout.set_fragment
    }

    override fun initData() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        initViews()
        return mView

    }

    var user: User? = null

    override fun onResume() {
        super.onResume()
        getNewNotificationNumber()
    }

    private fun initViews(){
        user = MMKVUtil.getObject(Constant.USER, User::class.java)
        user?.let {
            //1科室 2站岗位 3志愿者
            if(it.type == 1){
                viewBinding.rlSetTitle.setBackgroundResource(R.color.title_color)
                viewBinding.llContentBg.setBackgroundResource(R.drawable.personal_center_bg)
            }else if(it.type == 2){
                viewBinding.rlSetTitle.setBackgroundResource(R.color.orange)
                viewBinding.llContentBg.setBackgroundResource(R.color.orange)
            }else {
                viewBinding.rlSetTitle.setBackgroundResource(R.color.red)
                viewBinding.llContentBg.setBackgroundResource(R.color.red)
            }

            viewBinding.tvPersionCenterName.setText(user!!.name)
            Glide.with(activity)
                .load(Urls.BASE_IMG.toString() + user!!.imageurl)
                .thumbnail(0.1f)
                .apply(
                    RequestOptions().centerCrop()
                        .placeholder(R.mipmap.default_avatar)
                        .error(R.mipmap.default_avatar)
                )
                .into(viewBinding.personalCenterAvatar)
        }

        viewBinding.rlPersonalCenterOutLogin.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("提示")
            builder.setMessage("确定退出登陆吗？")
            builder.setPositiveButton(
                "确定"
            ) { dialog, which ->
                MMKVUtil.remove(Constant.USER)
                toActivity(LoginActivity::class.java)
                activity!!.finish()
            }
            builder.setNegativeButton("取消", null)
            builder.create().show()
        }
        viewBinding.rlPersonalCenterUpdatePassword.setOnClickListener {
            toActivity(UpdatePasswordActivity::class.java)
        }

        viewBinding.rlNotification.setOnClickListener {
            toActivity(NotificationActivity::class.java)
        }

    }

    val notificationServiceApi =
        NetWorkManager.getInstance().getServiceRequest(AlarmServiceApi::class.java)

    fun getNewNotificationNumber(){
        val date = MMKVUtil.get("Notification_DATE", "1990-01-01 00:00:00")
        NetWork.launch({ notificationServiceApi.getNewNotification(date) }, {
            if(it > 0){
                viewBinding.tvNotificationStatus.visibility = View.VISIBLE
            }else {
                viewBinding.tvNotificationStatus.visibility = View.GONE
            }
        }, {
            viewBinding.tvNotificationStatus.visibility = View.GONE
        })
    }


}