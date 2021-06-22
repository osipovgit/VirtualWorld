package com.osipov.evgeny.exception.handling;

public class ExceptionTemplate {

    private String exceptionInfo;

    public ExceptionTemplate(String info) {
        this.exceptionInfo = info;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

}
