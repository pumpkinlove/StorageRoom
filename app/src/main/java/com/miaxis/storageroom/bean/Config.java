package com.miaxis.storageroom.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xu.nan on 2017/6/29.
 */
@Entity
public class Config {
    @Id
    private long id;
    private String ip;
    private int port;
    private String orgCode;

    @Generated(hash = 649512384)
    public Config(long id, String ip, int port, String orgCode) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.orgCode = orgCode;
    }

    @Generated(hash = 589037648)
    public Config() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
}
