package com.hjsoft.emptransportstaff.model;

import java.io.Serializable;

/**
 * Created by hjsoft on 19/6/17.
 */
public class RescheduleData implements Serializable{

    String cmpId,empId,reqId,reqDate,repDate,repTime,remarks,repFrom,travelDeskStatus,allocationStatus,approvedBy,approvedDate;

    public RescheduleData(String cmpId,String empId,String reqId,String reqDate,String repDate,String repTime,
                          String remarks,String repFrom,String travelDeskStatus,String allocationStatus,String approvedBy,
                          String approvedDate)
    {
        this.cmpId=cmpId;
        this.empId=empId;
        this.reqId=reqId;
        this.reqDate=reqDate;
        this.repDate=repDate;
        this.repTime=repTime;
        this.remarks=remarks;
        this.repFrom=repFrom;
        this.travelDeskStatus=travelDeskStatus;
        this.allocationStatus=allocationStatus;
        this.approvedBy=approvedBy;
        this.approvedDate=approvedDate;
    }

    public String getCmpId() {
        return cmpId;
    }

    public void setCmpId(String cmpId) {
        this.cmpId = cmpId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getRepDate() {
        return repDate;
    }

    public void setRepDate(String repDate) {
        this.repDate = repDate;
    }

    public String getRepTime() {
        return repTime;
    }

    public void setRepTime(String repTime) {
        this.repTime = repTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRepFrom() {
        return repFrom;
    }

    public void setRepFrom(String repFrom) {
        this.repFrom = repFrom;
    }

    public String getTravelDeskStatus() {
        return travelDeskStatus;
    }

    public void setTravelDeskStatus(String travelDeskStatus) {
        this.travelDeskStatus = travelDeskStatus;
    }

    public String getAllocationStatus() {
        return allocationStatus;
    }

    public void setAllocationStatus(String allocationStatus) {
        this.allocationStatus = allocationStatus;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }
}
