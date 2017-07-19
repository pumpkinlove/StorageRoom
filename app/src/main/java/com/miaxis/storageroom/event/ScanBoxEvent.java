package com.miaxis.storageroom.event;

/**
 * Created by xu.nan on 2017/7/13.
 */

public class ScanBoxEvent {
    private String boxArray;

    public ScanBoxEvent(String boxArray) {
        this.boxArray = boxArray;
    }

    public String getBoxArray() {
        return boxArray;
    }

    public void setBoxArray(String boxArray) {
        this.boxArray = boxArray;
    }
}
