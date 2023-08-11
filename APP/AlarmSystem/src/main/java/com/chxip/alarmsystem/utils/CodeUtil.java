package com.chxip.alarmsystem.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码对比
 */
public class CodeUtil {
    /**
     * 将获取到的前端参数转为string类型
     * @param request
     * @param key
     * @return
     */
    public static String getString(HttpServletRequest request, String key) {
        try {
            String result = request.getParameter(key);
            if(result != null) {
                result = result.trim();
            }
            if("".equals(result)) {
                result = null;
            }
            return result;
        }catch(Exception e) {
            return null;
        }
    }
    /**
     * 验证码校验
     * @param code
     * @return
     */
    public static boolean checkVerifyCode(String code,String verifyCode) {

        if(code == null ||!verifyCode.equals(code)) {
            return false;
        }
        return true;
    }
}
