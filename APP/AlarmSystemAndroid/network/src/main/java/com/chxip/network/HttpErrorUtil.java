package com.chxip.network;

import android.net.ParseException;

import com.chxip.network.entity.ErrorResult;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;


public class HttpErrorUtil {

    public static ErrorResult getError(Throwable e){
        return new ErrorResult(500,exceptionHandler(e));
    }

    public static String exceptionHandler(Throwable e){
        String errorMsg = "网络错误！";
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            errorMsg = "当前网络不可用！";
        } else if (e instanceof SocketTimeoutException) {
            errorMsg = "网络请求超时！";
        } else if (e instanceof HttpException) {

            HttpException httpException = (HttpException) e;
            errorMsg = convertStatusCode(httpException);

        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            errorMsg = "数据解析错误！";
        }
        return errorMsg;
    }

    private static String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() >= 500 && httpException.code() < 600) {
            msg = "服务器处理请求出错";
        } else if (httpException.code() >= 400 && httpException.code() < 500) {
            msg = "网络出现错误";
        } else if (httpException.code() >= 300 && httpException.code() < 400) {
            msg = "服务器处理请求出错";
        }
        else {
            msg = httpException.message();
        }
        return msg;
    }
}
