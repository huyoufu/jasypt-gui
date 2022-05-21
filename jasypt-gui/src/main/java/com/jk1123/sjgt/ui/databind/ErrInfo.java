package com.jk1123.sjgt.ui.databind;

/**
 * @author huyoufu <https://github.com/huyoufu>
 * @TIME 2022/5/22 0:23
 * @description
 */
public class ErrInfo {
    private int code;
    private String msg;
    private Throwable exception;

    public ErrInfo() {
    }

    public ErrInfo(int code, String msg, Throwable exception) {
        this.code = code;
        this.msg = msg;
        this.exception = exception;
    }

    public ErrInfo(String msg, Throwable exception) {
        this.msg = msg;
        this.exception = exception;
    }

    public ErrInfo(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public ErrInfo setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ErrInfo setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Throwable getException() {
        return exception;
    }

    public ErrInfo setException(Throwable exception) {
        this.exception = exception;
        return this;
    }
}
