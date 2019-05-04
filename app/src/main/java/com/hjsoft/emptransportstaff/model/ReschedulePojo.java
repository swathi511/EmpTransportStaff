package com.hjsoft.emptransportstaff.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hjsoft on 19/6/17.
 */
public class ReschedulePojo {

    @SerializedName("companyid")
    @Expose
    private String companyid;
    @SerializedName("empid")
    @Expose
    private String empid;
    @SerializedName("reqid")
    @Expose
    private String reqid;
    @SerializedName("Reqdate")
    @Expose
    private String reqdate;
    @SerializedName("Repdate")
    @Expose
    private String repdate;
    @SerializedName("Reptime")
    @Expose
    private String reptime;
    @SerializedName("Remarks")
    @Expose
    private String  remarks;
    @SerializedName("RepFrom")
    @Expose
    private String repFrom;
    @SerializedName("traveldeskstatus")
    @Expose
    private String traveldeskstatus;
    @SerializedName("allocationstatus")
    @Expose
    private String allocationstatus;
    @SerializedName("approvedby")
    @Expose
    private String approvedby;
    @SerializedName("approveddate")
    @Expose
    private String approveddate;

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public String getReqdate() {
        return reqdate;
    }

    public void setReqdate(String reqdate) {
        this.reqdate = reqdate;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String  remarks) {
        this.remarks = remarks;
    }

    public String getRepFrom() {
        return repFrom;
    }

    public void setRepFrom(String repFrom) {
        this.repFrom = repFrom;
    }

    public String getTraveldeskstatus() {
        return traveldeskstatus;
    }

    public void setTraveldeskstatus(String traveldeskstatus) {
        this.traveldeskstatus = traveldeskstatus;
    }

    public String getAllocationstatus() {
        return allocationstatus;
    }

    public void setAllocationstatus(String allocationstatus) {
        this.allocationstatus = allocationstatus;
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
