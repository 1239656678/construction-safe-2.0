package com.dico.result;

/**
 * 统一API响应结果封装
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: HashUtils
 * 创建时间: 2018/12/13
 */
public class ResultData {
    private int code;
    private String msg;
    private Object data;

    public ResultData() {
        super();
    }

    public ResultData(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public ResultData setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultData setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ResultData setData(Object data) {
        this.data = data;
        return this;
    }

    public static ResultData getSuccessResult() {
        return new ResultData().setCode(0).setMsg("成功");
    }

    public static ResultData getSuccessResult(Object data) {
        return new ResultData().setCode(0).setMsg("成功").setData(data);
    }

    public static ResultData getFailResult(String message) {
        return new ResultData().setCode(1).setMsg(message);
    }

}
