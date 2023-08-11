package com.chxip.base_lib.util.sideslip;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fhf11991 on 2016/7/18.
 */

public class ActivityLifecycleHelper implements Application.ActivityLifecycleCallbacks {

    private static ActivityLifecycleHelper singleton;
    private static final Object lockObj = new Object();
    private List<Activity> activities;

    public List<Activity> getActivities() {
        return activities;
    }

    private ActivityLifecycleHelper() {
        activities = new LinkedList<>();
    }

    public static ActivityLifecycleHelper build() {
        synchronized (lockObj) {
            if (singleton == null) {
                singleton = new ActivityLifecycleHelper();
            }
            return singleton;
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }

        if (activities.size() == 0) {
            activities = null;
        }
    }


    /**
     * 获得Activity栈
     */
    public List<Activity> getActivityStack() {
        return activities;
    }

    /**
     * 清除栈队列中的所有Activity。
     */
    public void clearTask() {
        int size = activities.size();
        if (size > 0) {
            for (Activity activity : activities) {
                activity.finish();
            }
        }
    }

    /**
     * 从栈管理队列中移除该Activity。
     *
     * @param className Class。
     */
    public void removeFromStack(Class className) {
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).getClass() == className) {
                if (activities.get(i) != null) {
                    if(activities.get(i)!=null){
                        activities.get(i).finish();
                        activities.remove(activities.get(i));
                    }
                    break;
                }
            }
        }

    }

    /**
     * 查询Activity是否存在
     *
     * @param className Class。
     */
    public boolean isExistActivity(Class className) {
        boolean isExist=false;
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).getClass() == className) {
                if (activities.get(i) != null) {
                    isExist=true;
                    break;
                }
            }
        }
        return isExist;

    }

    /**
     * 从栈管理队列中移除该Activity。
     *
     * @param activity。
     */
    public void removeFromStack(Activity activity) {
        if(activity!=null){
            activities.remove(activity);
        }

    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activities == null) {
            activities = new LinkedList<>();
        }
        activities.add(activity);
    }

    /**
     * 获取集合中当前Activity
     *
     * @return
     */
    public Activity getLatestActivity() {
        ActivityLifecycleHelper adapter = build();
        int count = adapter.activities.size();
        if (count == 0) {
            return null;
        }
        return adapter.activities.get(count - 1);
    }

    /**
     * 获取集合中上一个Activity
     *
     * @return
     */
    public Activity getPreviousActivity() {
        ActivityLifecycleHelper adapter = build();
        int count = adapter.activities.size();
        if (count < 2) {
            return null;
        }
        return adapter.activities.get(count - 2);
    }
}
