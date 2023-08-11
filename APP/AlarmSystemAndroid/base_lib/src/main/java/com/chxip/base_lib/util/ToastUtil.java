/**
 *
 */
package com.chxip.base_lib.util;

import android.widget.Toast;

import com.chxip.base_lib.R;
import com.chxip.base_lib.application.BaseApplication;


public class ToastUtil {
    /**
     * 弹出Toast
     */
    public static void show(String info) {
        Toast.makeText(BaseApplication.getContext(), info, Toast.LENGTH_SHORT).show();
    }

    public static void show(int info) {
        Toast.makeText(BaseApplication.getContext(), info, Toast.LENGTH_SHORT).show();
    }

    /**
     * 网络访问失败 提示
     */
    public static void showNetworkError() {
        Toast.makeText(BaseApplication.getContext(), BaseApplication.getContext().getString(R.string.app_network_error), Toast.LENGTH_SHORT).show();
    }
}
