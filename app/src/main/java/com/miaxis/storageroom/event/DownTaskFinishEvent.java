package com.miaxis.storageroom.event;

/**
 * Created by xu.nan on 2017/7/13.
 */

public class DownTaskFinishEvent {

    private boolean success;
    private String message;

    public DownTaskFinishEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
