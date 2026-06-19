package com.rba.common.exceptions;


public class AppException extends RuntimeException {

    private Object errorData;

    private AppError error;

    public AppException(AppError err) {
        this(err, null, null);
    }

    public AppException(AppError err, Throwable t) {
        this(err, null, t);
    }

    public AppException(AppError err, String errMsg) {
        this(err, errMsg, null);
    }

    public AppException(AppError err, String errMsg, Throwable t) {
        super(t);
        this.error = err;
        this.errorData = errMsg;
    }

    public AppException(String msg) {
        errorData = msg;
    }

    public AppException(String msg, Throwable t) {
        super(t);
        errorData = msg;
    }

    public AppException(Throwable t) {
        super(t);
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(this.getClass().getName());
        sb.append(".superMsg[").append(super.getMessage());
        sb.append("].lderror[");
        if (this.error != null) {
            sb.append("<").append(this.error.name()).append(">");
        } else {
            sb.append("null");
        }
        sb.append("].errorData[");
        if (this.errorData != null) {
            sb.append(errorData);
        } else {
            sb.append("null");
        }
        sb.append("]}");
        return sb.toString();
    }


    public Object getErrorData() {
        return errorData;
    }

    public void setErrorData(Object errorData) {
        this.errorData = errorData;
    }

    public AppError getError() {
        return error;
    }

    public void setError(AppError error) {
        this.error = error;
    }

}
