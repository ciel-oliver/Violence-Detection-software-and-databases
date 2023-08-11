package com.chxip.base_lib.application;

import android.content.Context;
import android.os.Handler;

import androidx.multidex.MultiDex;

import com.bumptech.glide.util.Util;
import com.chxip.base_lib.BuildConfig;
import com.chxip.base_lib.util.sideslip.ActivityLifecycleHelper;

import org.litepal.LitePalApplication;

/**
 * @ClassName: BaseApplication
 * @Description: 整个项目的BaseApplication
 * @Author: chxip
 * @CreateDate: 2021/1/18 4:54 PM
 */
public class BaseApplication extends LitePalApplication {
    static Context context;


    public static Context getContext() {
        return context;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();

        //初始化滑动返回
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());



    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
