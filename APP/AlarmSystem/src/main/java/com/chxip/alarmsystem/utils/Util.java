package com.chxip.alarmsystem.utils;

import java.math.BigDecimal;

public class Util {

    /**
     * String 转 BigDecimal
     * @param str
     * @return
     */
    public static BigDecimal StringToBigDecimal(String str){
        return new BigDecimal(str);
    }

    /**
     * String 转 Int
     * @param str
     * @return
     */
    public static int StringToInt(String str){
        int value;
        try{
            value = Integer.parseInt(str);
        }catch (Exception e){
            value = 0;
        }
        return value;
    }


}
