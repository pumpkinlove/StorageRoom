package com.miaxis.storageroom.event;

import com.miaxis.storageroom.bean.Worker;

/**
 * Created by xu.nan on 2017/7/11.
 */

public class VerifyWorkerEvent {

    public static final int NO_WORKER = -1;
    public static final int SUCCESS = 0;
    public static final int FAIL = -2;

    private int result;
    private Worker worker;

    public VerifyWorkerEvent(int result, Worker worker) {
        this.result = result;
        this.worker = worker;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
