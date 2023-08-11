package com.chxip.alarmsystem.entity;

/**
 * 所有接口返回格式
 */
public class Response {

    //错误ID
    private int errorId;

    //具体数据
    private Object data;

    //错误Msg
    private String errorMsg;



    public Response() {
    }
    public Response(int errorId, String errorMsg) {
        this.errorId = errorId;
        this.errorMsg = errorMsg;
    }



    public Response(Object data) {
        this.data = data;
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
