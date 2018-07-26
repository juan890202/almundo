package com.almundo.test.dto.response;

import com.almundo.test.dto.request.Call;

public class ErrorResponse implements Response {

    private Call call;
    private boolean success;
    private String errorType;
    private String message;
    private DetailedError details;

    public static Response newResponse(Call call, Throwable cause) {
        return new ErrorResponse(call, cause);
    }

    private ErrorResponse(Call call, Throwable cause) {
        this.call = call;
        this.success = false;
        this.errorType = cause.getClass().getName();
        this.message = cause.getMessage();
        this.details = new DetailedError(cause);
    }

    @Override
    public Call getCall() {
        return call;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getErrorType() {
        return errorType;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public DetailedError getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "call=" + call +
                ", success=" + success +
                ", errorType='" + errorType + '\'' +
                ", message='" + message + '\'' +
                ", details=" + details +
                '}';
    }

}
