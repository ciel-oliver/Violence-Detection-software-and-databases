package com.ping.alarmsystem.activity.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.view.WindowManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.chxip.base_lib.base.MVBaseActivity
import com.chxip.base_lib.constant.Constant
import com.chxip.base_lib.util.*
import com.chxip.network.NetWork
import com.chxip.network.NetWorkManager
import com.ping.alarmsystem.MainActivity
import com.ping.alarmsystem.R
import com.ping.alarmsystem.api.LoginApiService
import com.ping.alarmsystem.databinding.ActivityLoginBinding
import com.ping.alarmsystem.databinding.RegisterViewBinding
import com.ping.alarmsystem.entity.User
import com.ping.alarmsystem.util.WisdomUtil
import com.zhouzun.base_lib.base.BaseActivity
import java.util.*


 class LoginActivity : BaseActivity<ActivityLoginBinding>() {


     val loginService = NetWorkManager.getInstance().getServiceRequest(LoginApiService::class.java);


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViews()


    }

    //注册布局
    var registerView: View? = null
     var dialog:Dialog? = null

    override fun initViews() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 全透明实现
            val window = getWindow()
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(Color.TRANSPARENT)
        }
        viewBinding.tvLoginWisdom.text = WisdomUtil.getWisdoms()


        viewBinding.btnLogin.setOnClickListener {
            val userName = viewBinding.etLoginUsername.text.toString()
            val password = viewBinding.etLoginPassword.text.toString()
            if (userName.isEmpty()) {
                ToastUtil.show("请输入用户名")
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                ToastUtil.show("请输入密码")
                return@setOnClickListener
            }
            dialog = DialogUtils.createLoadingDialog(this,"登录中....")
            dialog?.show()
            login(userName, password)
        }

        viewBinding.tvLoginForgetPassword.setOnClickListener {
            ToastUtil.show("请联系管理员")
        }


        val amOrPm: Int = GregorianCalendar().run {
            this.get(GregorianCalendar.AM_PM)
        }
        if (amOrPm == 0) {
            //上午
            viewBinding.ivLoginBg.setImageResource(R.drawable.good_morning_img)
           // viewBinding.tvLoginTime2.text = " Morning"
        } else {
            //下午
            viewBinding.ivLoginBg.setImageResource(R.drawable.good_night_img)
           // viewBinding.tvLoginTime2.text = " Night"
        }

        viewBinding.tvLoginRegister.setOnClickListener {
            //viewstub只能被inflate一次
            if (registerView == null) {
                initRegisterView()
            } else {
                viewStubBinding.llRegisterView.visibility = View.VISIBLE
            }
            showRegisterViewAnim()
        }

    }

     //登录
     fun login(userName: String, password: String) {
         NetWork.launch({ loginService.login(userName, password) }, { data ->
             //数据
             dialog?.dismiss()
             ToastUtil.show("登录成功")
             MMKVUtil.save(Constant.USER, GsonUtil.GSON.toJson(data))
             toActivity(MainActivity::class.java)
             finish()
         }, { error ->
             dialog?.dismiss()
         }, isShowLoading = true)
     }

     //注册
     fun register(userData:User) {
         NetWork.launch({ loginService.register(userData) }, { data ->
             //数据
             dialog?.dismiss()
             ToastUtil.show("登录成功")
             MMKVUtil.save(Constant.USER, GsonUtil.GSON.toJson(data))
             toActivity(MainActivity::class.java)
             finish()
         }, { error ->
             dialog?.dismiss()
         }, isShowLoading = true)
     }


    lateinit var viewStubBinding: RegisterViewBinding

    /**
     * 初始化注册布局
     */
    fun initRegisterView() {
        registerView = viewBinding.vsRegister.inflate()!!
        viewStubBinding = RegisterViewBinding.bind(registerView!!)

        viewStubBinding.tvRegisterLogin.setOnClickListener {
            viewBinding.llLoginView.visibility = View.VISIBLE
            showLoginViewAnim()

        }

        viewStubBinding.btnRegister.setOnClickListener {
            val userName = viewStubBinding.etRegisterUsername.text.toString()
            val password = viewStubBinding.etRegisterPassword.text.toString()
            val passwordConfirm = viewStubBinding.etRegisterPasswordConfirm.text.toString()
            val registerEmail = viewStubBinding.etRegisterEmail.text.toString()
            val realName = viewStubBinding.etRegisterRealname.text.toString()
            val phone = viewStubBinding.etRegisterPhone.text.toString()
            val typeMsg = viewStubBinding.etRegisterUserTypeMsg.text.toString()
            if (userName.isEmpty()) {
                ToastUtil.show("请输入用户名")
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                ToastUtil.show("请输入密码")
                return@setOnClickListener
            }
            if (passwordConfirm.isEmpty()) {
                ToastUtil.show("请确认密码")
                return@setOnClickListener
            }
            if(!password.equals(passwordConfirm)){
                ToastUtil.show("两次密码不一致")
                return@setOnClickListener
            }
            if (realName.isEmpty()) {
                ToastUtil.show("请输入真实名称")
                return@setOnClickListener
            }
            if (!registerEmail.contains("@")) {
                ToastUtil.show("邮箱格式不正确")
                return@setOnClickListener
            }
            when(viewStubBinding.rgRegisterType.checkedRadioButtonId){
                R.id.rb_register_type_yhry->{
                    if(typeMsg.isEmpty()){
                        ToastUtil.show("请输入科室号")
                        return@setOnClickListener
                    }

                }
                R.id.rb_register_type_zyz->{
                    if(typeMsg.isEmpty()) {
                        ToastUtil.show("请输入志愿者编号")
                        return@setOnClickListener
                    }
                }
                R.id.rb_register_type_zgw->{
                    if(typeMsg.isEmpty()) {
                        ToastUtil.show("请输入站岗号")
                        return@setOnClickListener
                    }
                }
            }
            dialog = DialogUtils.createLoadingDialog(this,"注册....")
            dialog?.show()
            val user = User();
            user.account = userName
            user.phone = phone
            user.password = password
            user.email = registerEmail
            user.name = realName
            user.userTypeMsg = typeMsg
            if(viewStubBinding.rgRegisterSex.checkedRadioButtonId == R.id.rb_register_type_nan){
                user.sex= "男"
            }else{
                user.sex= "女"
            }
            when (viewStubBinding.rgRegisterType.checkedRadioButtonId){
                R.id.rb_register_type_yhry ->{
                    user.type = 1
                }
                R.id.rb_register_type_zgw ->{
                    user.type = 2
                }
                R.id.rb_register_type_zyz ->{
                    user.type = 3
                }
            }
            register(user)
        }
    }


    /**
     * 显示注册view
     */
    fun showRegisterViewAnim() {
        val moveIn: ObjectAnimator = ObjectAnimator.ofFloat(viewStubBinding.llRegisterView, "translationX", 500f, 0f)
        val fadeInOut: ObjectAnimator = ObjectAnimator.ofFloat(viewStubBinding.llRegisterView, "alpha", 0f, 1f)
        val animSet = AnimatorSet()
        animSet.play(fadeInOut)
        animSet.play(moveIn)
        animSet.setDuration(500)
        animSet.start()


        val moveInLogin: ObjectAnimator = ObjectAnimator.ofFloat(viewBinding.llLoginView, "translationX", 0f, -500f)
        val fadeInOutLogin: ObjectAnimator = ObjectAnimator.ofFloat(viewBinding.llLoginView, "alpha", 1f, 0f)
        val animSetLogin = AnimatorSet()
        animSetLogin.play(moveInLogin)
        animSetLogin.play(fadeInOutLogin)
        animSetLogin.setDuration(500)
        animSetLogin.start()

    }


    /**
     * 显示登录view
     */
    fun showLoginViewAnim() {
        val moveIn: ObjectAnimator = ObjectAnimator.ofFloat(viewBinding.llLoginView, "translationX", -500f, 0f)
        val fadeInOut: ObjectAnimator = ObjectAnimator.ofFloat(viewBinding.llLoginView, "alpha", 0f, 1f)
        val animSet = AnimatorSet()
        animSet.play(fadeInOut)
        animSet.play(moveIn)
        animSet.setDuration(500)
        animSet.start()


        val moveInLogin: ObjectAnimator = ObjectAnimator.ofFloat(viewStubBinding.llRegisterView, "translationX", 0f,500f)
        val fadeInOutLogin: ObjectAnimator = ObjectAnimator.ofFloat(viewStubBinding.llRegisterView, "alpha", 1f, 0f)
        val animSetLogin = AnimatorSet()
        animSetLogin.play(moveInLogin)
        animSetLogin.play(fadeInOutLogin)
        animSetLogin.setDuration(500)
        animSetLogin.start()

    }
}
