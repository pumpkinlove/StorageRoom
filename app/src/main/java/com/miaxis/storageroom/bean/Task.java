package com.miaxis.storageroom.bean;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xu.nan on 2017/7/12.
 */
@Entity
public class Task implements Serializable {

    static final long serialVersionUID = 1L;
    @Id
    private long id;
    private String taskcode;
    private String taskseq;
    private String deptno;
    private String carid;
    private String plateno;
    private String taskdate;
    private String begintime;
    private String endtime;
    private String status;
    private String statusName;
    private String exetime;
    private String createuser;
    private String createusername;
    private String opuser;
    private String opusername;
    private String opdate;
    private String tasktype;
    private String tasklevel;
    private String seculevel;
    private String createtime;
    @Generated(hash = 1691270931)
    public Task(long id, String taskcode, String taskseq, String deptno,
            String carid, String plateno, String taskdate, String begintime,
            String endtime, String status, String statusName, String exetime,
            String createuser, String createusername, String opuser,
            String opusername, String opdate, String tasktype, String tasklevel,
            String seculevel, String createtime) {
        this.id = id;
        this.taskcode = taskcode;
        this.taskseq = taskseq;
        this.deptno = deptno;
        this.carid = carid;
        this.plateno = plateno;
        this.taskdate = taskdate;
        this.begintime = begintime;
        this.endtime = endtime;
        this.status = status;
        this.statusName = statusName;
        this.exetime = exetime;
        this.createuser = createuser;
        this.createusername = createusername;
        this.opuser = opuser;
        this.opusername = opusername;
        this.opdate = opdate;
        this.tasktype = tasktype;
        this.tasklevel = tasklevel;
        this.seculevel = seculevel;
        this.createtime = createtime;
    }
    @Generated(hash = 733837707)
    public Task() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTaskcode() {
        return this.taskcode;
    }
    public void setTaskcode(String taskcode) {
        this.taskcode = taskcode;
    }
    public String getTaskseq() {
        return this.taskseq;
    }
    public void setTaskseq(String taskseq) {
        this.taskseq = taskseq;
    }
    public String getDeptno() {
        return this.deptno;
    }
    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }
    public String getCarid() {
        return this.carid;
    }
    public void setCarid(String carid) {
        this.carid = carid;
    }
    public String getPlateno() {
        return this.plateno;
    }
    public void setPlateno(String plateno) {
        this.plateno = plateno;
    }
    public String getTaskdate() {
        return this.taskdate;
    }
    public void setTaskdate(String taskdate) {
        this.taskdate = taskdate;
    }
    public String getBegintime() {
        return this.begintime;
    }
    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }
    public String getEndtime() {
        return this.endtime;
    }
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatusName() {
        if (TextUtils.isEmpty(statusName)) {
            if ("1".equals(status)) {
                return "已上报";
            } else if ("2".equals(status)) {
                return "已审核";
            } else if ("3".equals(status)) {
                return "交接途中";
            } else if ("4".equals(status)) {
                return "已完成";
            } else if ("5".equals(status)) {
                return "已注销";
            }
        }
        return this.statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getExetime() {
        return this.exetime;
    }
    public void setExetime(String exetime) {
        this.exetime = exetime;
    }
    public String getCreateuser() {
        return this.createuser;
    }
    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }
    public String getCreateusername() {
        return this.createusername;
    }
    public void setCreateusername(String createusername) {
        this.createusername = createusername;
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
    public String getTasktype() {
        return this.tasktype;
    }
    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }
    public String getTasklevel() {
        return this.tasklevel;
    }
    public void setTasklevel(String tasklevel) {
        this.tasklevel = tasklevel;
    }
    public String getSeculevel() {
        return this.seculevel;
    }
    public void setSeculevel(String seculevel) {
        this.seculevel = seculevel;
    }
    public String getCreatetime() {
        return this.createtime;
    }
    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }


}
