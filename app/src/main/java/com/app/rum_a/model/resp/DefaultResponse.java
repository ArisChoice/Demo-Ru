package com.app.rum_a.model.resp;

/**
 * Created by harish on 31/8/18.
 */

public class DefaultResponse {
    private int Status;

    public void setMessage(String message) {
        Message = message;
    }

    private String Message;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getMessage() {
        return Message;
    }
}
