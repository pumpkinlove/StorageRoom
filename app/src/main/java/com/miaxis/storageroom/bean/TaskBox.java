package com.miaxis.storageroom.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by xu.nan on 2017/7/12.
 */
@Entity
public class TaskBox {

    public static final int NOT_VERIFIED = 1;
    public static final int VERIFIED = 2;
    public static final int REST = 3;

    @Transient
    private int status = NOT_VERIFIED;
    private String boxCode;
    private String boxRfid;
    private String taskCode;
    @Generated(hash = 3456399)
    public TaskBox(String boxCode, String boxRfid, String taskCode) {
        this.boxCode = boxCode;
        this.boxRfid = boxRfid;
        this.taskCode = taskCode;
    }
    @Generated(hash = 398843036)
    public TaskBox() {
    }
    public String getBoxCode() {
        return this.boxCode;
    }
    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }
    public String getBoxRfid() {
        return this.boxRfid;
    }
    public void setBoxRfid(String boxRfid) {
        this.boxRfid = boxRfid;
    }
    public String getTaskCode() {
        return this.taskCode;
    }
    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
