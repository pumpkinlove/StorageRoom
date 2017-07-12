package com.miaxis.storageroom.event;

/**
 * Created by xu.nan on 2017/7/12.
 */

public class DownEscortEvent {
    private int result;

    public DownEscortEvent(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
