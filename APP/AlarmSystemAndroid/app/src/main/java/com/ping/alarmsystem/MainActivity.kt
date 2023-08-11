package com.ping.alarmsystem

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.chxip.base_lib.util.*
import com.chxip.base_lib.util.sideslip.ActivityLifecycleHelper
import com.google.android.material.tabs.TabLayout
import com.ping.alarmsystem.activity.fragment.HomeFragment
import com.ping.alarmsystem.activity.fragment.MyFragment
import com.ping.alarmsystem.activity.fragment.SetFragment
import com.ping.alarmsystem.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(viewBinding.root)

        initViews()
    }


    override fun onResume() {
        super.onResume()


    }


    private fun initViews() {
        viewBinding.tlMainMenu.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            var oldTab: TabLayout.Tab? = null

            override fun onTabReselected(tab: TabLayout.Tab) {


            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                oldTab = tab
            }

            override fun onTabSelected(tab: TabLayout.Tab) {

                when (tab.position) {
                    0 -> {
                        tab.setIcon(R.mipmap.ic_bar_home_s)
                    }

                    1 -> {
                        tab.setIcon(R.mipmap.ic_bar_my_s)
                    }

                    2 -> {
                        tab.setIcon(R.mipmap.ic_bar_other_s)
                    }
                }
                if (oldTab != null) {
                    when (oldTab!!.position) {
                        0 -> oldTab!!.setIcon(R.mipmap.ic_bar_home)
                        1 -> oldTab!!.setIcon(R.mipmap.ic_bar_my)
                        2 -> oldTab!!.setIcon(R.mipmap.ic_bar_other)
                    }
                }
                changeIconImgBottomMargin(viewBinding.tlMainMenu)
            }
        })
        val fragmentList = mutableListOf<Fragment>()
        val homeFragment = HomeFragment()
        val myFragment = MyFragment()
        val setFragment = SetFragment()
        fragmentList.add(homeFragment)
        fragmentList.add(myFragment)
        fragmentList.add(setFragment)
        viewBinding.viewPageMain.adapter = MainMenuFragmentAdapter(
            supportFragmentManager,
            fragmentList,
            this,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewBinding.viewPageMain.offscreenPageLimit=3
        viewBinding.viewPageMain.currentItem = 0
        viewBinding.tlMainMenu.setupWithViewPager(viewBinding.viewPageMain)
        viewBinding.tlMainMenu.getTabAt(0)?.setIcon(R.mipmap.ic_bar_home_s)
        viewBinding.tlMainMenu.getTabAt(1)?.setIcon(R.mipmap.ic_bar_my)
        viewBinding.tlMainMenu.getTabAt(2)?.setIcon(R.mipmap.ic_bar_other)
        changeIconImgBottomMargin(viewBinding.tlMainMenu)

    }


    /**
     * 修改Tab 字体和图标之间的间距
     *
     * @param parent
     */
    private fun changeIconImgBottomMargin(parent: ViewGroup) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child is ViewGroup) {
                changeIconImgBottomMargin(child)
            } else if (child is ImageView) {
                val lp = child.getLayoutParams() as ViewGroup.MarginLayoutParams
                lp.bottomMargin = 2
                child.requestLayout()
            }
        }
    }

    class MainMenuFragmentAdapter(
        fm: FragmentManager?,
        var flist: List<Fragment>,
        var mContext: Context,
        behavior: Int
    ) :
        FragmentPagerAdapter(fm!!, behavior) {
        var title = arrayOf("首页",  "我的","设置")
        override fun getItem(arg0: Int): Fragment {
            // TODO Auto-generated method stub
            return flist[arg0]
        }

        override fun getCount(): Int {
            // TODO Auto-generated method stub
            return flist.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return title[position]
        }
    }


    /**
     * 连续按两次返回键就退出
     */
    private var keyBackClickCount = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            when (keyBackClickCount++) {
                0 -> {
                    ToastUtil.show(getString(R.string.press_again_exit))
                    val timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            keyBackClickCount = 0
                        }
                    }, 3000)
                }
                1 -> {
                    ActivityLifecycleHelper.build().clearTask()
                }
                else -> {
                }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}