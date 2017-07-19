package com.miaxis.storageroom.event;

import com.miaxis.storageroom.bean.Escort;

/**
 * Created by xu.nan on 2017/7/13.
 */

public class VerifyEscortEvent {
    private boolean success;
    private Escort escort;

    public VerifyEscortEvent(boolean success, Escort escort) {
        this.success = success;
        this.escort = escort;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Escort getEscort() {
        return escort;
    }

    public void setEscort(Escort escort) {
        this.escort = escort;
    }
}
