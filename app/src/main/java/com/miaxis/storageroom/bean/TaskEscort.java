package com.miaxis.storageroom.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xu.nan on 2017/7/12.
 */
@Entity
public class TaskEscort {

    private String taskCode;
    private String escortCode;
    @Generated(hash = 1445060504)
    public TaskEscort(String taskCode, String escortCode) {
        this.taskCode = taskCode;
        this.escortCode = escortCode;
    }
    @Generated(hash = 1268332439)
    public TaskEscort() {
    }
    public String getTaskCode() {
        return this.taskCode;
    }
    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }
    public String getEscortCode() {
        return this.escortCode;
    }
    public void setEscortCode(String escortCode) {
        this.escortCode = escortCode;
    }
}
