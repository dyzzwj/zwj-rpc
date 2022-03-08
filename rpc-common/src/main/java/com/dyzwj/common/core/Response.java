package com.dyzwj.common.core;

public class Response{

    private Integer requestId;
    private Integer code;
    private String info;

    private Object data;

    public static Response errorResponse(){
        Response res = new Response();
        res.setCode(1);
        res.setInfo("未正确连接到服务器.请检查相关配置信息!");
        return res;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
