package com.ping.alarmsystem.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.LinearLayout
import android.widget.TextView
import com.chxip.base_lib.constant.Constant
import com.chxip.base_lib.util.MMKVUtil
import com.ping.alarmsystem.MainActivity
import com.ping.alarmsystem.R
import com.ping.alarmsystem.activity.login.LoginActivity
import com.ping.alarmsystem.entity.User
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val intent = Intent(this, MainActivity::class.java)
        val intent1 = Intent(this, LoginActivity::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            delay(1000)
            withContext(Dispatchers.Main){
                val user = MMKVUtil.getObject(Constant.USER, User::class.java)
                if(user!=null){
                    startActivity(intent)
                }else{
                    startActivity(intent1)

                }
                finish()
            }
        }

    }
}
