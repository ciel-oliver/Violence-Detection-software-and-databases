package com.ping.alarmsystem.activity.fragment

import android.app.Activity
import android.app.Dialog
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chxip.base_lib.base.RecycleViewDivider
import com.chxip.base_lib.constant.Constant
import com.chxip.base_lib.util.DialogUtils
import com.chxip.base_lib.util.MMKVUtil
import com.chxip.base_lib.util.ToastUtil
import com.chxip.base_lib.util.statusbar.StatusBarUtil
import com.chxip.network.NetWork
import com.chxip.network.NetWorkManager
import com.google.android.material.tabs.TabLayout
import com.ping.alarmsystem.R
import com.ping.alarmsystem.adapter.AlarmNewAdapter
import com.ping.alarmsystem.api.AlarmServiceApi
import com.ping.alarmsystem.databinding.HomeFragmentBinding
import com.ping.alarmsystem.entity.Alarm
import com.ping.alarmsystem.entity.User
import com.zhouzun.base_lib.base.BaseFragment
import java.util.*

class HomeFragment : BaseFragment<HomeFragmentBinding>() {


    override fun getLayoutId(): Int {
        return R.layout.home_fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        user = MMKVUtil.getObject(Constant.USER, User::class.java)
        initViews()
        return mView
    }

    override fun onResume() {
        super.onResume()
        user = MMKVUtil.getObject(Constant.USER, User::class.java)
    }

    var user: User? = null
    var dialog:Dialog? = null
    val alarmServiceApi =
        NetWorkManager.getInstance().getServiceRequest(AlarmServiceApi::class.java)
    var alarmNewList : MutableList<Alarm>? = Collections.synchronizedList(mutableListOf())
    lateinit var alarmNewAdapter: AlarmNewAdapter

    var alarmLsList : MutableList<Alarm>? = Collections.synchronizedList(mutableListOf())
    lateinit var alarmLsAdapter: AlarmNewAdapter

    var timer: Timer? = null
    private var timerTask: TimerTask?  = null

    private fun initViews(){

        if(user == null){
            user = MMKVUtil.getObject(Constant.USER, User::class.java)
        }
        user?.let {
            //1科室 2站岗位 3志愿者
            if(it.type == 1){
                StatusBarUtil.setStatusBarColor(activity,R.color.title_color)
                viewBinding.rlHomeTitle.setBackgroundResource(R.color.title_color)
            }else if(it.type == 2){
                viewBinding.rlHomeTitle.setBackgroundResource(R.color.orange)
                StatusBarUtil.setStatusBarColor(activity,R.color.orange)
            }else {
                viewBinding.rlHomeTitle.setBackgroundResource(R.color.red)
                StatusBarUtil.setStatusBarColor(activity,R.color.red)
            }
        }

        viewBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                 if(tab.position == 0){
                     //新报警
                     getAlarm()
                     viewBinding.rvHomeNewAlarms.visibility = View.VISIBLE
                     viewBinding.srlHomeNewAlarms.visibility = View.VISIBLE
                     viewBinding.rvHomeLsAlarms.visibility = View.GONE
                     viewBinding.srlHomeLsAlarms.visibility = View.GONE

                 }else{
                     getAlarmLs()
                     //历史报警
                     viewBinding.rvHomeNewAlarms.visibility = View.GONE
                     viewBinding.rvHomeLsAlarms.visibility = View.VISIBLE
                     viewBinding.srlHomeNewAlarms.visibility = View.GONE
                     viewBinding.srlHomeLsAlarms.visibility = View.VISIBLE
                 }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        alarmNewList = mutableListOf();
        alarmNewAdapter = AlarmNewAdapter(alarmNewList!!,context!!)
        alarmNewAdapter.useId = user!!.id
        viewBinding.rvHomeNewAlarms.let {
            val linearLayoutManager = LinearLayoutManager(context!!)
            it.layoutManager = linearLayoutManager
            it.addItemDecoration( RecycleViewDivider(context!!, LinearLayoutManager.VERTICAL) )
            it.adapter = alarmNewAdapter
        }

        alarmNewAdapter.setOnItemClickListener { view, position ->
            updateAlarmUserId(alarmNewList!![position].alarmId)
        }
        viewBinding.srlHomeNewAlarms.setOnRefreshListener {
            //刷新
            getAlarm()
        }
        viewBinding.srlHomeNewAlarms.setEnableLoadMore(false)


        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                getAlarm()
            }
        }
        timer?.schedule(timerTask, 0, 2000);


        alarmLsList = mutableListOf();
        alarmLsAdapter = AlarmNewAdapter(alarmLsList!!,context!!)
        alarmLsAdapter.type = 2
        alarmLsAdapter.useId = user!!.id
        viewBinding.rvHomeLsAlarms.let {
            val linearLayoutManager = LinearLayoutManager(context!!)
            it.layoutManager = linearLayoutManager
            it.addItemDecoration( RecycleViewDivider(context!!, LinearLayoutManager.VERTICAL) )
            it.adapter = alarmLsAdapter
        }

        alarmLsAdapter.setOnItemClickListener { view, position ->
            //完成处理
            showAlarmDialog(alarmLsList!![position].alarmId)
        }
        viewBinding.srlHomeLsAlarms.setOnRefreshListener {
            //刷新
            getAlarmLs()
        }
        viewBinding.srlHomeNewAlarms.setEnableLoadMore(false)
    }


    override fun initData() {

    }

    var alarmDialog:Dialog? = null

    private fun showAlarmDialog(alarmId:Int){
        val view = layoutInflater.inflate(R.layout.alarm_dialog,null)
        val etAlarm  = view.findViewById<EditText>(R.id.et_alarm)
        val tvAlarm = view.findViewById<TextView>(R.id.tv_alarm)
        tvAlarm.setOnClickListener {
            val remark = etAlarm.text.toString()
            if(remark.isEmpty()){
                ToastUtil.show("请输入处理方式")
                return@setOnClickListener
            }
            updateAlarmStatus(alarmId,remark)
        }
        alarmDialog = Dialog(context) // 创建自定义样式dialog
        alarmDialog?.setContentView(view, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                              LinearLayout.LayoutParams.WRAP_CONTENT)) // 设置布局
        // 将对话框的大小按屏幕大小的百分比设置
        val windowManager = (context as Activity).windowManager
        val display = windowManager.defaultDisplay
        val lp = alarmDialog?.window?.attributes
        lp?.width = (display.width * 0.8).toInt() //设置宽度
        alarmDialog?.window?.attributes = lp
        alarmDialog?.setContentView(view)
        alarmDialog?.show()
    }

    fun getAlarm(){
        if(user == null){
            user = MMKVUtil.getObject(Constant.USER, User::class.java)
        }
        NetWork.launch({ alarmServiceApi.getAlarm(user!!.type) }, {
            viewBinding.srlHomeNewAlarms.finishRefresh()//结束刷新
            viewBinding.srlHomeNewAlarms.finishLoadMore()//结束加载
            if(it.isNotEmpty()){
                //震动
                val vib = mContext!!.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
                vib.vibrate(100)
            }
            alarmNewList!!.clear()
            alarmNewList!!.addAll(it)
            alarmNewAdapter.notifyDataSetChanged()
        }, {
            viewBinding.srlHomeNewAlarms.finishRefresh()//结束刷新
            viewBinding.srlHomeNewAlarms.finishLoadMore()//结束加载
        })
    }

    fun getAlarmLs(){
        NetWork.launch({ alarmServiceApi.getAlarmByUserId(1,10000,user!!.type) }, {
            viewBinding.srlHomeLsAlarms.finishRefresh()//结束刷新
            viewBinding.srlHomeLsAlarms.finishLoadMore()//结束加载
            alarmLsList!!.clear()
            alarmLsList!!.addAll(it)
            alarmLsAdapter.notifyDataSetChanged()
        }, {
            viewBinding.srlHomeLsAlarms.finishRefresh()//结束刷新
            viewBinding.srlHomeLsAlarms.finishLoadMore()//结束加载
        })
    }

    fun updateAlarmUserId(alarmId:Int){
        if(dialog == null){
            dialog = DialogUtils.createLoadingDialog(context!!,"提交数据....")
            dialog?.show()
        }

        NetWork.launch({ alarmServiceApi.updateAlarmUserId(user!!.id,alarmId) }, {
            dialog?.dismiss()
             ToastUtil.show("接受成功")
        }, {
            dialog?.dismiss()
        })
    }

    fun updateAlarmStatus(alarmId:Int,remark:String){
        if(dialog == null){
            dialog = DialogUtils.createLoadingDialog(context!!,"提交数据....")
            dialog?.show()
        }

        NetWork.launch({ alarmServiceApi.updateAlarmStatus(3,alarmId,remark) }, {
            dialog?.dismiss()
            alarmDialog?.dismiss()
            getAlarmLs()
            ToastUtil.show("处理完成")
        }, {
            dialog?.dismiss()
        })
    }

    override fun onDestroyView() {
        timer?.cancel()
        timerTask?.cancel()
        timer = null
        timerTask = null
        super.onDestroyView()

    }

}