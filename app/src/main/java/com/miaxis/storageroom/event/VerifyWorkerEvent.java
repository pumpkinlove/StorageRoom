package com.miaxis.storageroom.event;

import com.miaxis.storageroom.bean.Worker;

/**
 * Created by xu.nan on 2017/7/11.
 */

public class VerifyWorkerEvent {
    private boolean success;
    private Worker worker;

    public VerifyWorkerEvent(boolean success, Worker worker) {
        this.success = success;
        this.worker = worker;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
