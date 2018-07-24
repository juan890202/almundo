package com.almundo.test.dto.response;

import com.almundo.test.dto.request.Call;

public interface Response {

    Call getCall();

    boolean isSuccess();

    default String getErrorType() {return null;}

    String getMessage();

    default DetailedError getDetails() {
        return null;
    }

}
