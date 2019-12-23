package com.xhr.mca.common;

import com.xhr.mca.entity.constant.ExceptionConstants;

/**
 * 自定义异常类
 * 
 * @author Huang Sheng
 */
public class WebAppException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -5435794276394983984L;

    /**
     * 错误编码
     */
    private Integer errorCode;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 异常枚举
     */
    private ExceptionConstants eConstants;

    /**
     * 构造一个基本异常.
     *
     * @param message
     *            信息描述
     */
    public WebAppException(ExceptionConstants eConstants) {
        super();
        this.eConstants = eConstants;
        this.errorCode = eConstants.getErrorCode();
        this.message = eConstants.getErrorMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ExceptionConstants geteConstants() {
        return eConstants;
    }

    public void seteConstants(ExceptionConstants eConstants) {
        this.eConstants = eConstants;
    }

}
