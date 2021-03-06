package com.miaxis.storageroom.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xu.nan on 2017/6/29.
 */
@Entity
public class Escort implements Serializable {

    static final long serialVersionUID = 1L;
    @Id
    private long id;
    private String name;
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
    private String code;
    private byte[] photo;
    private String password;
    private String opDate;
    private String opUserCode;
    private String opUserName;
    private boolean isCollected;
    private String phone;
    private String idCard;

    @Generated(hash = 1199856996)
    public Escort(long id, String name, String finger0, String finger1,
            String finger2, String finger3, String finger4, String finger5,
            String finger6, String finger7, String finger8, String finger9,
            String code, byte[] photo, String password, String opDate,
            String opUserCode, String opUserName, boolean isCollected, String phone,
            String idCard) {
        this.id = id;
        this.name = name;
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
        this.code = code;
        this.photo = photo;
        this.password = password;
        this.opDate = opDate;
        this.opUserCode = opUserCode;
        this.opUserName = opUserName;
        this.isCollected = isCollected;
        this.phone = phone;
        this.idCard = idCard;
    }

    @Generated(hash = 295686502)
    public Escort() {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpDate() {
        return opDate;
    }

    public void setOpDate(String opDate) {
        this.opDate = opDate;
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

    public void setFinger(String mFinger, int index) {
        switch (index) {
            case 0:
                finger0 = mFinger;
                break;
            case 1:
                finger1 = mFinger;
                break;
            case 2:
                finger2 = mFinger;
                break;
            case 3:
                finger3 = mFinger;
                break;
            case 4:
                finger4 = mFinger;
                break;
            case 5:
                finger5 = mFinger;
                break;
            case 6:
                finger6 = mFinger;
                break;
            case 7:
                finger7 = mFinger;
                break;
            case 8:
                finger8 = mFinger;
                break;
            case 9:
                finger9 = mFinger;
                break;
            default:
        }
    }

    public String getOpUserCode() {
        return opUserCode;
    }

    public void setOpUserCode(String opUserCode) {
        this.opUserCode = opUserCode;
    }

    public String getOpUserName() {
        return opUserName;
    }

    public void setOpUserName(String opUserName) {
        this.opUserName = opUserName;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getIsCollected() {
        return this.isCollected;
    }

    public void setIsCollected(boolean isCollected) {
        this.isCollected = isCollected;
    }
}
