package com.myzuji.backend.dto;

import java.io.Serializable;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public class ResponseInfo implements Serializable {

    private static final long serialVersionUID = -1744281762151876960L;

    private String code;
    private String message;

    public ResponseInfo(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
