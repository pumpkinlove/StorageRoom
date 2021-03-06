package com.miaxis.storageroom.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Created by xu.nan on 2017/7/11.
 */
@Entity
public class Worker implements Serializable {

    static final long serialVersionUID = 1L;

    @Id
    private long id;
    private String name;
    @Unique
    private String code;
    private String orgCode;
    private String phone;
    private String idCard;
    private String opUser;
    private String opUserName;
    private String opDate;
    private String status;

    private String finger0;
    private String finger1;
    private String finger2;
    private String finger3;
    private String finger4;
    private String finger5;
    private String finger6;
    private String finger7;
    private String finger8;
    private String finger9;

    @Generated(hash = 427290544)
    public Worker(long id, String name, String code, String orgCode, String phone,
            String idCard, String opUser, String opUserName, String opDate,
            String status, String finger0, String finger1, String finger2,
            String finger3, String finger4, String finger5, String finger6,
            String finger7, String finger8, String finger9) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.orgCode = orgCode;
        this.phone = phone;
        this.idCard = idCard;
        this.opUser = opUser;
        this.opUserName = opUserName;
        this.opDate = opDate;
        this.status = status;
        this.finger0 = finger0;
        this.finger1 = finger1;
        this.finger2 = finger2;
        this.finger3 = finger3;
        this.finger4 = finger4;
        this.finger5 = finger5;
        this.finger6 = finger6;
        this.finger7 = finger7;
        this.finger8 = finger8;
        this.finger9 = finger9;
    }

    @Generated(hash = 107771754)
    public Worker() {
    }

    public String getFinger(int index) {
        switch (index) {
            case 0:
                return finger0;
            case 1:
                return finger1;
            case 2:
                return finger2;
            case 3:
                return finger3;
            case 4:
                return finger4;
            case 5:
                return finger5;
            case 6:
                return finger6;
            case 7:
                return finger7;
            case 8:
                return finger8;
            case 9:
                return finger9;
            default:
                return null;
        }
    }

    public String setFinger(String mFinger, int index) {
        switch (index) {
            case 0:
                finger0 = mFinger;
            case 1:
                finger1 = mFinger;
            case 2:
                finger2 = mFinger;
            case 3:
                finger3 = mFinger;
            case 4:
                finger4 = mFinger;
            case 5:
                finger5 = mFinger;
            case 6:
                finger6 = mFinger;
            case 7:
                finger7 = mFinger;
            case 8:
                finger8 = mFinger;
            case 9:
                finger9 = mFinger;
            default:
                return null;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFinger0() {
        return finger0;
    }

    public void setFinger0(String finger0) {
        this.finger0 = finger0;
    }

    public String getFinger1() {
        return finger1;
    }

    public void setFinger1(String finger1) {
        this.finger1 = finger1;
    }

    public String getFinger2() {
        return finger2;
    }

    public void setFinger2(String finger2) {
        this.finger2 = finger2;
    }

    public String getFinger3() {
        return finger3;
    }

    public void setFinger3(String finger3) {
        this.finger3 = finger3;
    }

    public String getFinger4() {
        return finger4;
    }

    public void setFinger4(String finger4) {
        this.finger4 = finger4;
    }

    public String getFinger5() {
        return finger5;
    }

    public void setFinger5(String finger5) {
        this.finger5 = finger5;
    }

    public String getFinger6() {
        return finger6;
    }

    public void setFinger6(String finger6) {
        this.finger6 = finger6;
    }

    public String getFinger7() {
        return finger7;
    }

    public void setFinger7(String finger7) {
        this.finger7 = finger7;
    }

    public String getFinger8() {
        return finger8;
    }

    public void setFinger8(String finger8) {
        this.finger8 = finger8;
    }

    public String getFinger9() {
        return finger9;
    }

    public void setFinger9(String finger9) {
        this.finger9 = finger9;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return this.idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getOpUser() {
        return this.opUser;
    }

    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }

    public String getOpUserName() {
        return this.opUserName;
    }

    public void setOpUserName(String opUserName) {
        this.opUserName = opUserName;
    }

    public String getOpDate() {
        return this.opDate;
    }

    public void setOpDate(String opDate) {
        this.opDate = opDate;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}