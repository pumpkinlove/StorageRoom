package com.miaxis.storageroom.event;

/**
 * Created by xu.nan on 2017/7/13.
 */

public class GetFingerEvent {
    private String finger;

    public GetFingerEvent(String finger) {
        this.finger = finger;
    }

    public String getFinger() {
        return finger;
    }

    public void setFinger(String finger) {
        this.finger = finger;
    }
}
