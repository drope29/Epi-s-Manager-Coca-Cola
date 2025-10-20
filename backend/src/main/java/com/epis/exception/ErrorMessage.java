package com.epis.exception;

public class ErrorMessage {

    private String path;
    private String method;
    private int status;
    private String error;
    private String message;

    public ErrorMessage(){}

    public ErrorMessage(String path, String method, int status, String error, String message) {
        this.path = path;
        this.method = method;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

}
