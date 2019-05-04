package com.hjsoft.emptransportstaff.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hjsoft on 19/6/17.
 */
public class ReportPojo {

    @SerializedName("empid")
    @Expose
    private String empid;
    @SerializedName("Reqdate")
    @Expose
    private String reqdate;
    @SerializedName("RepId")
    @Expose
    private String repId;
    @SerializedName("Repdate")
    @Expose
    private String repdate;
    @SerializedName("Reptime")
    @Expose
    private String reptime;
    @SerializedName("vehusedtype")
    @Expose
    private String vehusedtype;
    @SerializedName("Routename")
    @Expose
    private String routename;
    @SerializedName("traveldeskstatus")
    @Expose
    private String traveldeskstatus;
    @SerializedName("approvedby")
    @Expose
    private String approvedby;
    @SerializedName("approveddate")
    @Expose
    private String approveddate;

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getReqdate() {
        return reqdate;
    }

    public void setReqdate(String reqdate) {
        this.reqdate = reqdate;
    }

    public String getRepId() {
        return repId;
    }

    public void setRepId(String repId) {
        this.repId = repId;
    }

    public String getRepdate() {
        return repdate;
    }

    public void setRepdate(String repdate) {
        this.repdate = repdate;
    }

    public String getReptime() {
        return reptime;
    }

    public void setReptime(String reptime) {
        this.reptime = reptime;
    }

    public String getVehusedtype() {
        return vehusedtype;
    }

    public void setVehusedtype(String vehusedtype) {
        this.vehusedtype = vehusedtype;
    }

    public String getRoutename() {
        return routename;
    }

    public void setRoutename(String routename) {
        this.routename = routename;
    }

    public String getTraveldeskstatus() {
        return traveldeskstatus;
    }

    public void setTraveldeskstatus(String traveldeskstatus) {
        this.traveldeskstatus = traveldeskstatus;
    }

    public String getApprovedby() {
        return approvedby;
    }

    public void setApprovedby(String approvedby) {
        this.approvedby = approvedby;
    }

    public String getApproveddate() {
        return approveddate;
    }

    public void setApproveddate(String approveddate) {
        this.approveddate = approveddate;
    }
}
