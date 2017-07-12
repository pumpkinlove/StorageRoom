package com.miaxis.storageroom.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by xu.nan on 2017/7/3.
 */
@Entity
public class TimeStamp {
    @Id
    private long id;            // 1 worker  2 escort 3 box
    private String stampName;
    private String opDateTime;

    @Generated(hash = 1772844379)
    public TimeStamp(long id, String stampName, String opDateTime) {
        this.id = id;
        this.stampName = stampName;
        this.opDateTime = opDateTime;
    }

    @Generated(hash = 1112509906)
    public TimeStamp() {
    }

    public String getStampName() {
        return stampName;
    }

    public void setStampName(String stampName) {
        this.stampName = stampName;
    }

    public String getOpDateTime() {
        return opDateTime;
    }

    public void setOpDateTime(String opDateTime) {
        this.opDateTime = opDateTime;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
