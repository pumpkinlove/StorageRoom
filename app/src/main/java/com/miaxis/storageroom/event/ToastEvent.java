package com.miaxis.storageroom.event;

/**
 * Created by xu.nan on 2017/7/11.
 */

public class ToastEvent {
    private String message;

    public ToastEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
