package com.miaxis.storageroom.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xu.nan on 2017/7/12.
 */
@Entity
public class Box implements Serializable {

    private static final long serialVersionUID = 4502533188196635811L;
    @Id
    private long id;
    private String boxcode;
    private String boxname;
    private String deptno;
    private String rfid;
    private String opuser; /* 操作员 */
    private String opusername;
    private String opdate; /* 操作时间 */
    private String money;
    /**
     * 箱包状态：1-库房 2-网点 3-出库途中 4-入库途中 5-调拨途中
     */
    private String status;
    private String statusName;
    /**
     * 是否验证：0-未验证 1-已验证
     */
    @Transient
    private String checkStatus;
    @Transient
    private String checkStatusName;
    @Generated(hash = 1180561660)
    public Box(long id, String boxcode, String boxname, String deptno, String rfid,
            String opuser, String opusername, String opdate, String money,
            String status, String statusName) {
        this.id = id;
        this.boxcode = boxcode;
        this.boxname = boxname;
        this.deptno = deptno;
        this.rfid = rfid;
        this.opuser = opuser;
        this.opusername = opusername;
        this.opdate = opdate;
        this.money = money;
        this.status = status;
        this.statusName = statusName;
    }
    @Generated(hash = 1015730713)
    public Box() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getBoxcode() {
        return this.boxcode;
    }
    public void setBoxcode(String boxcode) {
        this.boxcode = boxcode;
    }
    public String getBoxname() {
        return this.boxname;
    }
    public void setBoxname(String boxname) {
        this.boxname = boxname;
    }
    public String getDeptno() {
        return this.deptno;
    }
    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }
    public String getRfid() {
        return this.rfid;
    }
    public void setRfid(String rfid) {
        this.rfid = rfid;
    }
    public String getOpuser() {
        return this.opuser;
    }
    public void setOpuser(String opuser) {
        this.opuser = opuser;
    }
    public String getOpusername() {
        return this.opusername;
    }
    public void setOpusername(String opusername) {
        this.opusername = opusername;
    }
    public String getOpdate() {
        return this.opdate;
    }
    public void setOpdate(String opdate) {
        this.opdate = opdate;
    }
    public String getMoney() {
        return this.money;
    }
    public void setMoney(String money) {
        this.money = money;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatusName() {
        return this.statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}
