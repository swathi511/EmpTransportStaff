package com.hjsoft.emptransportstaff.webservices;

import com.google.gson.JsonObject;
import com.hjsoft.emptransportstaff.model.LatLngPojo;
import com.hjsoft.emptransportstaff.model.Pojo;
import com.hjsoft.emptransportstaff.model.ReportPojo;
import com.hjsoft.emptransportstaff.model.ReschedulePojo;
import com.hjsoft.emptransportstaff.model.TripPojo;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by hjsoft on 7/6/17.
 */
public interface API {

    @GET("Employee/GetEmpTrip")
    Call<List<TripPojo>> getEmpTrips(@Query("login") String name,
                                     @Query("pwd") String pwd,
                                     @Query("companycode") String companyCode);

    @POST("Employee/Emplogin")
    Call<Pojo> userLogin(@Body JsonObject v);

    @GET("EmpTrip/GetTripTracking")
    Call<List<LatLngPojo>> getCoordinates(@Query("tripid") String tripId,
                                          @Query("companyid") String companyId);

    @PUT("EmpTrip/UpdateEmpVehStatus")
    Call<Pojo> sendReportingStatus(@Body JsonObject v);

    @POST("EmpTrip/EmpScheduleReq")
    Call<Pojo> reschedule(@Body JsonObject v);

    @GET("EmpReport/GetEmpReport")
    Call<List<ReportPojo>> getReportingHistory(@Query("companyid") String companyId,
                                               @Query("employeeid") String empId);
    @GET("EmpScheduleReq/GetEmpScheduleReq")
    Call<List<ReschedulePojo>> getRescheduleHistory(@Query("companyid") String companyId,
                                                    @Query("employeeid") String empId);

    @PUT("EmpReport/UpdateEmpReport")
    Call<Pojo> updateReportingStatus(@Body JsonObject v);

    @PUT("EmpScheduleReq/UpdateEmpScheduleReq")
    Call<Pojo> updateReschedule(@Body JsonObject v);
}
