package com.ping.alarmsystem.activity.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.*
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chxip.base_lib.activity.ClipImageActivity
import com.chxip.base_lib.constant.Constant
import com.chxip.base_lib.util.DateUtils
import com.chxip.base_lib.util.MLog
import com.chxip.base_lib.util.MMKVUtil
import com.chxip.base_lib.util.statusbar.StatusBarUtil
import com.chxip.network.NetWork
import com.chxip.network.NetWorkManager
import com.chxip.network.Urls
import com.ping.alarmsystem.R
import com.ping.alarmsystem.activity.NotificationActivity
import com.ping.alarmsystem.activity.login.LoginActivity
import com.ping.alarmsystem.activity.my.UpdateActivity
import com.ping.alarmsystem.activity.my.UpdatePasswordActivity
import com.ping.alarmsystem.api.AlarmServiceApi
import com.ping.alarmsystem.api.LoginApiService
import com.ping.alarmsystem.databinding.MyFragmentBinding
import com.ping.alarmsystem.entity.User
import com.ping.wegame.api.UploadFileServiceApi
import com.yanzhenjie.permission.AndPermission
import com.zhouzun.base_lib.base.BaseFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MyFragment : BaseFragment<MyFragmentBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.my_fragment

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        createCameraTempFile(savedInstanceState)
        initViews()
        return mView

    }

    override fun initData() {

    }


    var user: User? = null

    override fun onResume() {
        super.onResume()
        sxUser()

    }

    fun sxUser(){
        user = MMKVUtil.getObject(Constant.USER, User::class.java)
        user?.let {
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
            //1科室 2站岗位 3志愿者
            if(it.type == 1){
                viewBinding.rlMyTitle.setBackgroundResource(R.color.title_color)
                viewBinding.llContentBg.setBackgroundResource(R.drawable.personal_center_bg)
            }else if(it.type == 2){
                viewBinding.rlMyTitle.setBackgroundResource(R.color.orange)
                viewBinding.llContentBg.setBackgroundResource(R.color.orange)
            }else {
                viewBinding.rlMyTitle.setBackgroundResource(R.color.red)
                viewBinding.llContentBg.setBackgroundResource(R.color.red)
            }
        }
        viewBinding.tvUserEmail.setText("邮箱：" + user?.email)
        viewBinding.tvUserPhone.setText("手机号："+user?.phone)
        viewBinding.tvUserBirthday.setText("生日："+user?.birthday?.replace(" 00:00:00",""))
        viewBinding.tvUserSex.text = "性别："+user?.sex
        var userType = ""
        //1科室 2站岗位 3志愿者
        when(user?.type){
            1->{
                userType = "科室："
                viewBinding.tvUserType.text = "类型：医护人员"
            }
            2->{
                userType = "站岗位："
                viewBinding.tvUserType.text = "类型：安保人员"
            }
            3->{
                userType = "志愿者："
                viewBinding.tvUserType.text = "类型：志愿者"
            }
        }
        viewBinding.tvUserTypeMsg.text = userType + user?.userTypeMsg
    }

    fun initViews() {


        viewBinding.personalCenterAvatar.setOnClickListener {
            // 先判断是否有权限。
            // 先判断是否有权限。
            if (AndPermission.hasPermission(
                    activity!!,
                    Manifest.permission.CAMERA
                ) && AndPermission.hasPermission(
                    activity!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                showPop()
            } else {
                // 申请权限。
                AndPermission.with(activity!!)
                    .requestCode(100)
                    .permission(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .send()
            }
        }

        viewBinding.rlPersonalCenterUpdate.setOnClickListener {
            toActivity(UpdateActivity::class.java)
        }

    }


    //请求相机
    private val REQUEST_CAPTURE = 100

    //请求相册
    private val REQUEST_PICK = 101

    //请求截图
    private val REQUEST_CROP_PHOTO = 102

    //调用照相机返回图片临时文件
    private var tempFile: File? = null


    var settlementPop: PopupWindow? = null

    private fun showPop() {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.my_information_pop, null)
        val rl_mip_xc = view.findViewById<View>(R.id.rl_mip_xc) as RelativeLayout
        val rl_mip_pz = view.findViewById<View>(R.id.rl_mip_pz) as RelativeLayout
        val rl_mip_qx = view.findViewById<View>(R.id.rl_mip_qx) as RelativeLayout
        settlementPop = PopupWindow(
            view,
            ViewPager.LayoutParams.MATCH_PARENT,
            ViewPager.LayoutParams.WRAP_CONTENT
        )
        settlementPop!!.isOutsideTouchable = true
        settlementPop!!.setBackgroundDrawable(BitmapDrawable())
        //防止PopupWindow被软件盘挡住
        settlementPop!!.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        settlementPop!!.isTouchable = true
        //更改透明度
        val params: WindowManager.LayoutParams = activity!!.getWindow().getAttributes()
        params.alpha = 0.5f
        activity!!.getWindow().setAttributes(params)

        // 设置可以获取焦点，否则弹出菜单中的EditText是无法获取输入的
        settlementPop!!.isFocusable = true
        // 设置弹出窗体显示时的动画，从底部向上弹出
        settlementPop!!.animationStyle = R.style.mypopwindow_anim_style
        settlementPop!!.showAtLocation(viewBinding.llPersonalCenter, Gravity.BOTTOM, 0, 0)
        settlementPop!!.setOnDismissListener {
            val params: WindowManager.LayoutParams = activity!!.getWindow().getAttributes()
            params.alpha = 1f
            activity!!.getWindow().setAttributes(params)
        }
        rl_mip_xc.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, REQUEST_PICK)
        }
        rl_mip_pz.setOnClickListener {
            /**
             * 跳转到照相机
             */
            /**
             * 跳转到照相机
             */
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoOutputUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    activity!!,
                    activity!!.getPackageName().toString() + ".provider",
                    tempFile!!
                )
            } else {
                Uri.fromFile(tempFile)
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoOutputUri)
            startActivityForResult(intent, REQUEST_CAPTURE)
        }
        rl_mip_qx.setOnClickListener { settlementPop!!.dismiss() }
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private fun createCameraTempFile(savedInstanceState: Bundle?) {
        tempFile = if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            savedInstanceState.getSerializable("tempFile") as File
        } else {
            File(
                checkDirPath(Environment.getExternalStorageDirectory().path + "/image/"),
                System.currentTimeMillis().toString() + ".jpg"
            )
        }
        MLog.i("tempFile:" + tempFile!!.path)
    }

    /**
     * 检查文件是否存在
     */
    private fun checkDirPath(dirPath: String): String? {
        if (TextUtils.isEmpty(dirPath)) {
            return ""
        }
        val dir = File(dirPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dirPath
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("tempFile", tempFile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (settlementPop != null) {
            settlementPop!!.dismiss()
        }
        when (requestCode) {
            REQUEST_CAPTURE -> if (resultCode == FragmentActivity.RESULT_OK) {
                gotoClipActivity(Uri.fromFile(tempFile))
            }
            REQUEST_PICK -> if (resultCode == FragmentActivity.RESULT_OK) {
                val imageUri = data?.data
                gotoClipActivity(imageUri)
            }
            REQUEST_CROP_PHOTO -> if (resultCode == FragmentActivity.RESULT_OK) {
                val uri = data?.data ?: return
                val cropImagePath: String? =
                    getRealFilePathFromUri(activity!!.getApplicationContext(), uri)
                MLog.i("URI:$cropImagePath")
                compress(cropImagePath)
            }
        }
    }

    fun compress(imagePath: String?) {

        val uploadFileServiceApi = NetWorkManager.getInstance().getServiceRequest(
            UploadFileServiceApi::class.java
        )
        val file = File(imagePath)
        val requestFile: RequestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", "file", requestFile)

        NetWork.launch({ uploadFileServiceApi.uploadImage(body) }, {
            user!!.imageurl = it
            MMKVUtil.saveObject(Constant.USER, user)
            Glide.with(activity)
                .load(Urls.BASE_IMG.toString() + user!!.imageurl)
                .thumbnail(0.1f)
                .apply(
                    RequestOptions().centerCrop()
                        .placeholder(R.mipmap.default_avatar)
                        .error(R.mipmap.default_avatar)
                )
                .into(viewBinding.personalCenterAvatar)
            updateUser(user!!)
        }, {

        })

    }

    fun updateUser(user: User) {
        val loginApiService =
            NetWorkManager.getInstance().getServiceRequest(LoginApiService::class.java)

        NetWork.launch({ loginApiService.updateUser(user) }, {

        }, {

        })
    }


    /**
     * 打开截图界面
     *
     * @param uri
     */
    fun gotoClipActivity(uri: Uri?) {
        if (uri == null) {
            return
        }
        val intent = Intent()
        intent.setClass(activity, ClipImageActivity::class.java)
        intent.data = uri
        startActivityForResult(
            intent,
            REQUEST_CROP_PHOTO
        )
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    fun getRealFilePathFromUri(context: Context, uri: Uri?): String? {
        if (null == uri) return null
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null) data = uri.path else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor = context.contentResolver.query(
                uri,
                arrayOf(MediaStore.Images.ImageColumns.DATA),
                null,
                null,
                null
            )
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }


}