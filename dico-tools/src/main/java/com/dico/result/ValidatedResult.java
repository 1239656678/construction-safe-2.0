package com.dico.result;

public class ValidatedResult {
    private boolean status = true;
    private String message = "";

    public boolean isStatus() {
        return status;
    }

    public ValidatedResult setStatus(boolean status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ValidatedResult setMessage(String message) {
        this.message = message;
        return this;
    }

}
