package com.miaxis.storageroom.event;

/**
 * Created by xu.nan on 2017/7/11.
 */

public class DownWorkerEvent {

    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;
    public static final int NO_WORKER = 2;

    private int result;

    public DownWorkerEvent(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }



}
