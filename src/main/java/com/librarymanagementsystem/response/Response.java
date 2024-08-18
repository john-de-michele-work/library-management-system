package com.librarymanagementsystem.response;

import java.util.List;

public class Response {

    private final int code;
    private final String message;
    private final List<String> errorMessages;

    public Response(int code, String message, List<String> errorMessages) {
        this.code = code;
        this.message = message;
        this.errorMessages = errorMessages;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
