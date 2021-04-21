package com.osipov_evgeny.exception_handling;

public class ExceptionTemplate {
    private String exceptionInfo;

    public ExceptionTemplate() {
    }

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
