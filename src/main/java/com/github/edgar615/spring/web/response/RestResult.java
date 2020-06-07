package com.github.edgar615.spring.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

public class RestResult implements Serializable {

    private static final Integer SUCCESS_CODE = 0;

    private static final String SUCCESS_MSG = "sucess";

    private final Integer code;

    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Object data;

    private RestResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static RestResult success() {
        return success(null);
    }

    public static RestResult success(Object data) {
        return new RestResult(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    /**
     * 返回失败，但是http响应码为200
     * @param code 错误码
     * @param message 消息
     * @return RestResult
     */
    public static RestResult failed(Integer code, String message) {
        return failed(SUCCESS_CODE, SUCCESS_MSG, null);
    }

    /**
     * 返回失败，但是http响应码为200
     * @param code 错误码
     * @param message 消息
     * @param data 数据
     * @return RestResult
     */
    public static RestResult failed(Integer code, String message, Object data) {
        return new RestResult(code, message, data);
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
