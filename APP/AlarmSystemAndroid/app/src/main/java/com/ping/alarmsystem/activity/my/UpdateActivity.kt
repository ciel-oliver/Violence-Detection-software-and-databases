package com.ping.alarmsystem.activity.my

import android.app.Dialog
import android.os.Bundle
import com.chxip.base_lib.constant.Constant
import com.chxip.base_lib.util.DateUtils
import com.chxip.base_lib.util.DialogUtils
import com.chxip.base_lib.util.MMKVUtil
import com.chxip.base_lib.util.ToastUtil
import com.chxip.network.NetWork
import com.chxip.network.NetWorkManager
import com.ping.alarmsystem.R
import com.ping.alarmsystem.activity.login.LoginActivity
import com.ping.alarmsystem.api.LoginApiService
import com.ping.alarmsystem.databinding.ActivityUpdateBinding
import com.ping.alarmsystem.entity.User
import com.zhouzun.base_lib.base.BaseActivity

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener

import android.widget.DatePicker
import com.chxip.base_lib.util.statusbar.StatusBarUtil
import java.util.*


class UpdateActivity : BaseActivity<ActivityUpdateBinding>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        setTitle("修改信息")
        user = MMKVUtil.getObject(Constant.USER, User::class.java)!!
        initViews()

    }
    var  user : User?= null
    var dialog: Dialog? = null
    var str: String? = null

    override fun initViews() {

        user?.let {
            //1科室 2站岗位 3志愿者
            if(it.type == 1){
                StatusBarUtil.setStatusBarColor(this,R.color.title_color)
                viewBinding.rlUpdateTitle.setBackgroundResource(R.color.title_color)
            }else if(it.type == 2){
                viewBinding.rlUpdateTitle.setBackgroundResource(R.color.orange)
                StatusBarUtil.setStatusBarColor(this,R.color.orange)
            }else {
                viewBinding.rlUpdateTitle.setBackgroundResource(R.color.red)
                StatusBarUtil.setStatusBarColor(this,R.color.red)
            }
        }


        viewBinding.etUpdateBirthday.setOnClickListener {
            val mcalendar: Calendar = Calendar.getInstance() //  获取当前时间    —   年、月、日
            val year: Int = mcalendar.get(Calendar.YEAR) //  得到当前年
            val month: Int = mcalendar.get(Calendar.MONTH) //  得到当前月
            val day: Int = mcalendar.get(Calendar.DAY_OF_MONTH) //  得到当前日
            DatePickerDialog(this@UpdateActivity,
                { view, year, month, dayOfMonth ->
                    //  日期选择对话框
                    //  这个方法是得到选择后的 年，月，日，分别对应着三个参数 — year、month、dayOfMonth
                    viewBinding.etUpdateBirthday.text = year.toString() + "-" + month + "-" + dayOfMonth
                }, year, month, day
            ).show() //  弹出日历对话框时，默认显示 年，月，日

        }

        if(user == null){
            ToastUtil.show("请先登录")
            //去登陆
            toActivity(LoginActivity::class.java)
        }else{
            viewBinding.etUpdateRealname.setText(user?.name)
            viewBinding.etUpdateEmali.setText(user?.email)
            viewBinding.etUpdatePhone.setText(user?.phone)
            viewBinding.etUpdateBirthday.setText(user?.birthday?.replace(" 00:00:00",""))
            if(user?.sex == "女"){
                viewBinding.rgUpdateSex.check(R.id.rb_register_type_nv)
            }else{
                viewBinding.rgUpdateSex.check(R.id.rb_register_type_nan)
            }
            viewBinding.llUpdate.setOnClickListener {
                val realname = viewBinding.etUpdateRealname.text.toString()
                val phone = viewBinding.etUpdatePhone.text.toString()
                val email = viewBinding.etUpdateEmali.text.toString()

                if(realname.isEmpty()){
                    ToastUtil.show("姓名不能为空")
                    return@setOnClickListener
                }
                if(phone.isEmpty()){
                    ToastUtil.show("手机号不能为空")
                    return@setOnClickListener
                }
                if(email.isEmpty()){
                    ToastUtil.show("邮箱不能为空")
                    return@setOnClickListener
                }
                if (!email.contains("@")) {
                    ToastUtil.show("邮箱格式不正确")
                    return@setOnClickListener
                }


                dialog = DialogUtils.createLoadingDialog(this)
                dialog?.show()
                val loginApiService = NetWorkManager.getInstance().getServiceRequest(LoginApiService::class.java)
                user?.name = realname
                user?.phone = phone
                user?.email = email
                user?.birthday = "${viewBinding.etUpdateBirthday.text} 00:00:00"
                if(viewBinding.rgUpdateSex.checkedRadioButtonId == R.id.rb_register_type_nan){
                    user?.sex = "男"
                }else{
                    user?.sex = "女"
                }
                NetWork.launch({loginApiService.updateUser(user!!)},{
                    dialog?.dismiss()
                    ToastUtil.show("修改成功,请重新登录")
                    MMKVUtil.saveObject(Constant.USER,user)
                    finish()
                },{
                    dialog?.dismiss()
                })


            }
        }

    }
}