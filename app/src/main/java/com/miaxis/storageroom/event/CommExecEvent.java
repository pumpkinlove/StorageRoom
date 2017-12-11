package com.miaxis.storageroom.event;

/**
 * Created by xu.nan on 2017/12/11.
 */

public class CommExecEvent {
    public static final int COMM_ADD_WORKER = 115;
    public static final int COMM_COUNT_ESCORT = 128;
    public static final int COMM_DELTE_ESCORT = 132;
    public static final int COMM_DOWN_ESCORT = 125;
    public static final int COMM_DOWN_TASK = 126;
    public static final int COMM_DOWN_WORKER = 127;
    public static final int COMM_EXEC_TASK = 129;
    public static final int COMM_UPDATE_ESCORT = 130;

    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_EXCEPTION = -2017;
    public static final int RESULT_SOCKET_NULL = -2018;

    private int result;
    private int commCode;

    public CommExecEvent(int result, int commCode) {
        this.result = result;
        this.commCode = commCode;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getCommCode() {
        return commCode;
    }

    public void setCommCode(int commCode) {
        this.commCode = commCode;
    }
}
