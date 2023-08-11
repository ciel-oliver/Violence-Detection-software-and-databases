package com.chxip.base_lib.util;

import android.content.res.Configuration;
import android.content.res.Resources;

/**
 * @ClassName: SystemUtil
 * @Description: 涉及到系统的一些设置
 * @Author: chxip
 * @CreateDate: 4/15/21 5:48 PM
 */
public class SystemUtil {
    /**
     * 修改APP字体
     *
     * @param resource  即context.getResources()
     * @param fontScale  1.0即为标准大小，不随系统缩放
     */
    public static void configFontScale(Resources resource, float fontScale) {
        if (resource == null) {
            return;
        }
        try {
            Configuration c = resource.getConfiguration();
            c.fontScale = fontScale; //字体缩放设置为1.0
            resource.updateConfiguration(c, resource.getDisplayMetrics());
        } catch (Exception e) {
            //do what you want
        }
    }
}
