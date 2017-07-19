package com.miaxis.storageroom.event;

/**
 * Created by xu.nan on 2017/7/11.
 */

public class AddWorkerEvent {

    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;

    private int result;

    public AddWorkerEvent(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }



}
