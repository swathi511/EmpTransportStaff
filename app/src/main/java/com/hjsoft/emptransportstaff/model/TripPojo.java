package com.hjsoft.emptransportstaff.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hjsoft on 12/6/17.
 */
public class TripPojo {

    @SerializedName("EmployeeId")
    @Expose
    private String employeeId;
    @SerializedName("EmployeeName")
    @Expose
    private String employeeName;
    @SerializedName("companyid")
    @Expose
    private String companyid;
    @SerializedName("customerid")
    @Expose
    private String customerid;
    @SerializedName("tripid")
    @Expose
    private String tripid;
    @SerializedName("tripdate")
    @Expose
    private String tripdate;
    @SerializedName("vehcatgid")
    @Expose
    private String vehcatgid;
    @SerializedName("VehicleRegId")
    @Expose
    private String vehicleRegId;
    @SerializedName("othervehicle")
    @Expose
    private String othervehicle;
    @SerializedName("driverid")
    @Expose
    private String driverid;
    @SerializedName("otherdriver")
    @Expose
    private String otherdriver;
    @SerializedName("RouteID")
    @Expose
    private String routeID;
    @SerializedName("RouteName")
    @Expose
    private String routeName;
    @SerializedName("roosterno")
    @Expose
    private String roosterno;
    @SerializedName("tripstatus")
    @Expose
    private String tripstatus;
    @SerializedName("preparedby")
    @Expose
    private String preparedby;
    @SerializedName("modifieddate")
    @Expose
    private String modifieddate;
    @SerializedName("drivername")
    @Expose
    private String drivername;
    @SerializedName("drivermobile")
    @Expose
    private String drivermobile;
    @SerializedName("driverdutystatus")
    @Expose
    private String driverdutystatus;
    @SerializedName("gpsroutedetails")
    @Expose
    private Object gpsroutedetails;
    @SerializedName("gpstotkms")
    @Expose
    private Integer gpstotkms;
    @SerializedName("gpdtothrs")
    @Expose
    private Integer gpdtothrs;
    @SerializedName("VehicleRegNo")
    @Expose
    private String vehicleRegNo;
    @SerializedName("vehcategory")
    @Expose
    private String vehcategory;
    @SerializedName("longitude")
    @Expose
    private Object longitude;
    @SerializedName("latitude")
    @Expose
    private Object latitude;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getTripid() {
        return tripid;
    }

    public void setTripid(String tripid) {
        this.tripid = tripid;
    }

    public String getTripdate() {
        return tripdate;
    }

    public void setTripdate(String tripdate) {
        this.tripdate = tripdate;
    }

    public String getVehcatgid() {
        return vehcatgid;
    }

    public void setVehcatgid(String vehcatgid) {
        this.vehcatgid = vehcatgid;
    }

    public String getVehicleRegId() {
        return vehicleRegId;
    }

    public void setVehicleRegId(String vehicleRegId) {
        this.vehicleRegId = vehicleRegId;
    }

    public String getOthervehicle() {
        return othervehicle;
    }

    public void setOthervehicle(String othervehicle) {
        this.othervehicle = othervehicle;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public String getOtherdriver() {
        return otherdriver;
    }

    public void setOtherdriver(String otherdriver) {
        this.otherdriver = otherdriver;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRoosterno() {
        return roosterno;
    }

    public void setRoosterno(String roosterno) {
        this.roosterno = roosterno;
    }

    public String getTripstatus() {
        return tripstatus;
    }

    public void setTripstatus(String tripstatus) {
        this.tripstatus = tripstatus;
    }

    public String getPreparedby() {
        return preparedby;
    }

    public void setPreparedby(String preparedby) {
        this.preparedby = preparedby;
    }

    public String getModifieddate() {
        return modifieddate;
    }

    public void setModifieddate(String modifieddate) {
        this.modifieddate = modifieddate;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getDrivermobile() {
        return drivermobile;
    }

    public void setDrivermobile(String drivermobile) {
        this.drivermobile = drivermobile;
    }

    public String getDriverdutystatus() {
        return driverdutystatus;
    }

    public void setDriverdutystatus(String driverdutystatus) {
        this.driverdutystatus = driverdutystatus;
    }

    public Object getGpsroutedetails() {
        return gpsroutedetails;
    }

    public void setGpsroutedetails(Object gpsroutedetails) {
        this.gpsroutedetails = gpsroutedetails;
    }

    public Integer getGpstotkms() {
        return gpstotkms;
    }

    public void setGpstotkms(Integer gpstotkms) {
        this.gpstotkms = gpstotkms;
    }

    public Integer getGpdtothrs() {
        return gpdtothrs;
    }

    public void setGpdtothrs(Integer gpdtothrs) {
        this.gpdtothrs = gpdtothrs;
    }

    public String getVehicleRegNo() {
        return vehicleRegNo;
    }

    public void setVehicleRegNo(String vehicleRegNo) {
        this.vehicleRegNo = vehicleRegNo;
    }

    public String getVehcategory() {
        return vehcategory;
    }

    public void setVehcategory(String vehcategory) {
        this.vehcategory = vehcategory;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

}

