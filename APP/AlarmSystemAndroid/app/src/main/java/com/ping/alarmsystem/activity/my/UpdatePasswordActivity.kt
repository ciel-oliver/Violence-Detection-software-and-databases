package com.ping.alarmsystem.activity.my

import android.app.Dialog
import android.os.Bundle
import com.chxip.base_lib.constant.Constant
import com.chxip.base_lib.util.DialogUtils
import com.chxip.base_lib.util.MMKVUtil
import com.chxip.base_lib.util.ToastUtil
import com.chxip.base_lib.util.statusbar.StatusBarUtil
import com.chxip.network.NetWork
import com.chxip.network.NetWorkManager
import com.ping.alarmsystem.R
import com.ping.alarmsystem.activity.login.LoginActivity
import com.ping.alarmsystem.api.LoginApiService
import com.ping.alarmsystem.databinding.ActivityUpdatePasswordBinding
import com.ping.alarmsystem.entity.User
import com.zhouzun.base_lib.base.BaseActivity

class UpdatePasswordActivity : BaseActivity<ActivityUpdatePasswordBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)
        setTitle("修改密码")
        user = MMKVUtil.getObject(Constant.USER, User::class.java)!!
        initViews()
    }
    lateinit var  user :User
    var dialog: Dialog? = null

    override fun initViews() {

        user?.let {
            //1科室 2站岗位 3志愿者
            if(it.type == 1){
                StatusBarUtil.setStatusBarColor(this,R.color.title_color)
                viewBinding.rlUpdatePasswordTitle.setBackgroundResource(R.color.title_color)
            }else if(it.type == 2){
                viewBinding.rlUpdatePasswordTitle.setBackgroundResource(R.color.orange)
                StatusBarUtil.setStatusBarColor(this,R.color.orange)
            }else {
                viewBinding.rlUpdatePasswordTitle.setBackgroundResource(R.color.red)
                StatusBarUtil.setStatusBarColor(this,R.color.red)
            }
        }

        viewBinding.llUpdatePasswordDetermine.setOnClickListener {
            val newPassword = viewBinding.etUpdatePasswordNew.text.toString()
            val newPasswordConfirm = viewBinding.etUpdatePasswordNewConfirm.text.toString()
            val oldPassword = viewBinding.etUpdatePasswordOriginal.text.toString()
            if(oldPassword.isEmpty()){
                ToastUtil.show("请输入旧密码")
                return@setOnClickListener
            }
            if(newPassword.isEmpty()){
                ToastUtil.show("请输入新密码")
                return@setOnClickListener
            }
            if(newPasswordConfirm.isEmpty()){
                ToastUtil.show("请输入确定密码")
                return@setOnClickListener
            }
            if(newPassword != newPasswordConfirm){
                ToastUtil.show("两次密码不一致")
                return@setOnClickListener
            }
            dialog = DialogUtils.createLoadingDialog(this)
            dialog?.show()
            val loginApiService = NetWorkManager.getInstance().getServiceRequest(LoginApiService::class.java)

            NetWork.launch({loginApiService.updatePassword(newPassword,oldPassword,user.id)},{
                dialog?.dismiss()
                ToastUtil.show("修改成功,请重新登录")
                MMKVUtil.remove(Constant.USER)
                toActivity(LoginActivity::class.java)

            },{
                dialog?.dismiss()
            })
        }
    }



}