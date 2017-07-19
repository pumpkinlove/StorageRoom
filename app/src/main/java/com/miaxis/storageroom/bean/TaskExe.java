package com.miaxis.storageroom.bean;

import java.io.Serializable;

/**
 * Created by xu.nan on 2017/7/13.
 */

public class TaskExe implements Serializable {

    static final long serialVersionUID = 1L;

    private String taskcode;
    private String tasktype;
    private String deptno;
    private String workercode;
    private String workername;
    private String escode1;
    private String esname1;
    private String escode2;
    private String esname2;
    private String carcode;
    private String plateno;
    private String tasktime;
    private String status;

    public String getTaskcode() {
        return taskcode;
    }

    public void setTaskcode(String taskcode) {
        this.taskcode = taskcode;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getWorkercode() {
        return workercode;
    }

    public void setWorkercode(String workercode) {
        this.workercode = workercode;
    }

    public String getWorkername() {
        return workername;
    }

    public void setWorkername(String workername) {
        this.workername = workername;
    }

    public String getEscode1() {
        return escode1;
    }

    public void setEscode1(String escode1) {
        this.escode1 = escode1;
    }

    public String getEsname1() {
        return esname1;
    }

    public void setEsname1(String esname1) {
        this.esname1 = esname1;
    }

    public String getEscode2() {
        return escode2;
    }

    public void setEscode2(String escode2) {
        this.escode2 = escode2;
    }

    public String getEsname2() {
        return esname2;
    }

    public void setEsname2(String esname2) {
        this.esname2 = esname2;
    }

    public String getCarcode() {
        return carcode;
    }

    public void setCarcode(String carcode) {
        this.carcode = carcode;
    }

    public String getPlateno() {
        return plateno;
    }

    public void setPlateno(String plateno) {
        this.plateno = plateno;
    }

    public String getTasktime() {
        return tasktime;
    }

    public void setTasktime(String tasktime) {
        this.tasktime = tasktime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
