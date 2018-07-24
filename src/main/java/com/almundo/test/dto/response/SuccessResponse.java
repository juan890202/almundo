package com.almundo.test.dto.response;

import com.almundo.test.dto.request.Call;

public class SuccessResponse implements Response {

    private Call call;
    private boolean success;
    private String message;

    public static Response newResponse(Call call) {
        return new SuccessResponse(call);
    }

    private SuccessResponse(Call call) {
        this.call = call;
        this.success = true;
        this.message = "Call was attended successfully !!!";
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
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "SuccessResponse{" +
                "call=" + call +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
