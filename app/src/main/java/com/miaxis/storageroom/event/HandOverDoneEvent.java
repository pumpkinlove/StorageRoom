package com.miaxis.storageroom.event;

/**
 * Created by xu.nan on 2017/7/14.
 */

public class HandOverDoneEvent {
    private int result;

    public HandOverDoneEvent(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
