package com.gwghk.crm.exception;

import com.gwghk.crm.common.ApiErrorCode;

//必须继承RuntimeException，这样事务才能在抛出这个异常时发生事务回滚
public class SystemException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String code;
    private String msg;

    public SystemException(String code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public SystemException(ApiErrorCode e) {
        super(e.getDesc());
        this.code = e.getValue();
        this.msg = e.getDesc();
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

}
