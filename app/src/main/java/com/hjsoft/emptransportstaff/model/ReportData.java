package com.hjsoft.emptransportstaff.model;

/**
 * Created by hjsoft on 19/6/17.
 */
public class ReportData {

    String empId,reqDate,repId,repDate,repTime,vehUsedType,routeName,status,approvedBy,approvedDate;


    public ReportData(String empId,String  reqDate,String repId,String repDate,String repTime,String vehUsedType,String routeName,String status
            ,String approvedBy,String approvedDate)
    {
        this.empId=empId;
        this.reqDate=reqDate;
        this.repId=repId;
        this.repDate=repDate;
        this.repTime=repTime;
        this.vehUsedType=vehUsedType;
        this.routeName=routeName;
        this.status=status;
        this.approvedBy=approvedBy;
        this.approvedDate=approvedDate;
    }


    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getRepId() {
        return repId;
    }

    public void setRepId(String repId) {
        this.repId = repId;
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

    public String getVehUsedType() {
        return vehUsedType;
    }

    public void setVehUsedType(String vehUsedType) {
        this.vehUsedType = vehUsedType;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
