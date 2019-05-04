package com.hjsoft.emptransportstaff.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonObject;
import com.hjsoft.emptransportstaff.R;
import com.hjsoft.emptransportstaff.SessionManager;
import com.hjsoft.emptransportstaff.activity.ReportingHistoryActivity;
import com.hjsoft.emptransportstaff.activity.RescheduleActivity;
import com.hjsoft.emptransportstaff.activity.RescheduleHistoryActivity;
import com.hjsoft.emptransportstaff.activity.TrackCabActivity;
import com.hjsoft.emptransportstaff.adapter.DrawerItemCustomAdapter;
import com.hjsoft.emptransportstaff.model.Pojo;
import com.hjsoft.emptransportstaff.model.TripPojo;
import com.hjsoft.emptransportstaff.webservices.API;
import com.hjsoft.emptransportstaff.webservices.RestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoft on 20/6/17.
 */
public class TripSummaryFragment extends Fragment {

    View v;
    TextView tvStaffName,tvOfficeVeh,tvOwnVeh;
    TextView tvMyTrips,tvRouteName,tvDate,tvMnth,tvTime,tvDriverName,tvVehno;
    API REST_CLIENT;
    String uname,pwd,companyCode="CMP0001";
    RelativeLayout rLayout;
    SimpleDraweeView ivStaff;
    HashMap<String, String> user;
    SessionManager session;
    LayoutInflater inflater;
    List<TripPojo> tripList;
    TripPojo trip;
    DatePicker dp;
    Button ok;
    ImageView cancel;
    int day,mnth,yr;
    String stSelectedTrip,stTripId;
    TextView tvReschedule;
    TextView tvTrackRide,tvCallDriver;
    String driverMobile;
    String companyId="CMP0001";
    ProgressDialog progressDialog;
    String empId;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "sp";
    String stDate;
    AlertDialog alertDialog;
    TextView tvRefresh;
    AlertDialog.Builder dialogBuilder;
    View dialogView;
    TextView tvTodayDate;

    /////////////

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    DrawerItemCustomAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.activity_trip_summary, container, false);

        ivStaff=(SimpleDraweeView)v.findViewById(R.id.ats_iv_driver);
        tvStaffName=(TextView)v.findViewById(R.id.ats_tv_staff_name);
        tvOfficeVeh=(TextView)v.findViewById(R.id.ats_tv_company_vehicle);
        tvOwnVeh=(TextView)v.findViewById(R.id.ats_tv_own_vehicle);
        tvMyTrips=(TextView)v.findViewById(R.id.ats_tv_my_trips);
        tvRouteName=(TextView)v.findViewById(R.id.ats_tv_route_name);
        tvDate=(TextView)v.findViewById(R.id.ats_tv_date);
        tvMnth=(TextView)v.findViewById(R.id.ats_tv_month);
        tvTime=(TextView)v.findViewById(R.id.ats_tv_time);
        tvDriverName=(TextView)v.findViewById(R.id.ats_tv_driver_name);
        tvVehno=(TextView)v.findViewById(R.id.ats_tv_veh_reg_no);
        tvReschedule=(TextView)v.findViewById(R.id.ats_tv_reschedule);
        tvCallDriver=(TextView)v.findViewById(R.id.ats_tv_call_driver);
        rLayout=(RelativeLayout)v.findViewById(R.id.ats_rlayout);
        tvReschedule=(TextView)v.findViewById(R.id.ats_tv_reschedule);
        tvTrackRide=(TextView)v.findViewById(R.id.ats_tv_track_car);
        tvCallDriver=(TextView)v.findViewById(R.id.ats_tv_call_driver);
        tvRefresh=(TextView)v.findViewById(R.id.ats_tv_refresh);
        tvRefresh.setVisibility(View.GONE);
        tvTodayDate=(TextView)v.findViewById(R.id.ats_tv_today_date);

        pref = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        empId=pref.getString("empId",null);

        String path = "res:/" + R.drawable.pic;
        ivStaff.setImageURI(Uri.parse(path));

        inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogView = inflater.inflate(R.layout.alert_date, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();

        REST_CLIENT= RestClient.get();

        session=new SessionManager(getActivity());
        user = session.getUserDetails();

        uname=user.get(SessionManager.KEY_NAME);
        pwd=user.get(SessionManager.KEY_PWD);

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        String formattedDate = df.format(c.getTime());
        tvTodayDate.setText(formattedDate);

//        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Roboto-Thin.ttf");
//
//        tvStaffName.setTypeface(custom_font);


        tvStaffName.setText(uname);

        rLayout.setVisibility(View.GONE);

        getEmpTrips();

        tvOfficeVeh.setClickable(false);


        tvOfficeVeh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvOwnVeh.setTextColor(Color.parseColor("#39a799"));
                tvOwnVeh.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_accent_stroke_nc_single_bg));

                tvOfficeVeh.setTextColor(Color.parseColor("#ffffff"));
                tvOfficeVeh.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_accent_solid_nc_bg));

                //-----------------------------------------------------

                alertDialog.show();


                dp = (DatePicker) dialogView.findViewById(R.id.datePicker);
                ok = (Button) dialogView.findViewById(R.id.ad_bt_ok);
                cancel=(ImageView)dialogView.findViewById(R.id.ad_bt_cancel);

                long now = System.currentTimeMillis() - 1000;

                dp.setMinDate(now);
                dp.setMaxDate(now + (1000 * 60 * 60 * 24 * 2));

                day = dp.getDayOfMonth();
                mnth = dp.getMonth() + 1;
                yr = dp.getYear();

                //System.out.println("date is " + day + ":" + mnth + ":" + yr);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                dp.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        day = dayOfMonth;
                        mnth = month + 1;
                        yr = year;

                        //System.out.println("date is " + day + ":::" + mnth + ":::" + yr);
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String stDate=mnth+"/"+day+"/"+yr;

                        stSelectedTrip="-";

                        System.out.println("selected trip id i s"+stSelectedTrip);

                        reportingMeans("Company",stSelectedTrip,stDate);
                        //alertDialog.dismiss();


                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                    }
                });
            }
        });

        tvOwnVeh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                alertDialog.show();


                dp = (DatePicker) dialogView.findViewById(R.id.datePicker);
                ok = (Button) dialogView.findViewById(R.id.ad_bt_ok);
                cancel=(ImageView) dialogView.findViewById(R.id.ad_bt_cancel);

                long now = System.currentTimeMillis() - 1000;

                dp.setMinDate(now);
                dp.setMaxDate(now + (1000 * 60 * 60 * 24 * 2));

                day = dp.getDayOfMonth();
                mnth = dp.getMonth() + 1;
                yr = dp.getYear();

                //System.out.println("date is " + day + ":" + mnth + ":" + yr);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                dp.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        day = dayOfMonth;
                        mnth = month + 1;
                        yr = year;

                        //System.out.println("date is " + day + ":::" + mnth + ":::" + yr);
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        stDate=mnth+"/"+day+"/"+yr;

                        if(trip!=null) {

                            try {
                                SimpleDateFormat newformat = new SimpleDateFormat("MM/dd/yyyy");
                                String datestring = trip.getModifieddate().split("T")[0];
                                SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd");
                                String reformattedStr = newformat.format(oldformat.parse(datestring));

                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

                                Date convertedDate = new Date();
                                Date convertedDate2 = new Date();
                                try {
                                    convertedDate = dateFormat.parse(reformattedStr);
                                    convertedDate2 = dateFormat.parse(stDate);

                                    if(convertedDate.equals(convertedDate2))
                                    {
                                        stSelectedTrip = stTripId;
                                        System.out.println("hello");
                                    }
                                    else {
                                        stSelectedTrip = "-";
                                    }
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }


//                                if (reformattedStr.equals(stDate)) {
//                                    stSelectedTrip = stTripId;
//                                } else {
//                                    stSelectedTrip = "-";
//                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        else {

                            stSelectedTrip = "-";
                        }

                        System.out.println("selected trip id i s"+stSelectedTrip);
                        reportingMeans("Own",stSelectedTrip,stDate);



                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                    }
                });
            }
        });


        tvReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getActivity(),RescheduleActivity.class);
                i.putExtra("empId",empId);
                startActivity(i);
                getActivity().finish();
            }
        });

        tvTrackRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(),"Please wait ...",Toast.LENGTH_SHORT).show();

                Intent i=new Intent(getActivity(),TrackCabActivity.class);
                i.putExtra("tripId",stTripId);
                startActivity(i);
                getActivity().finish();
            }
        });

        tvCallDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+driverMobile));//GUEST NUMBER HERE...
                startActivity(intent);

            }
        });

        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvRefresh.setVisibility(View.GONE);

                getEmpTrips();
            }
        });

        return  v;
    }


    public void getEmpTrips()
    {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        Call<List<TripPojo>> call=REST_CLIENT.getEmpTrips(uname,pwd,companyCode);
        call.enqueue(new Callback<List<TripPojo>>() {
            @Override
            public void onResponse(Call<List<TripPojo>> call, Response<List<TripPojo>> response) {



                if(response.isSuccessful())
                {
                    tvMyTrips.setText("My Trips");
                    tvMyTrips.setTextSize(15);
                    rLayout.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    tripList=response.body();
                    trip=tripList.get(0);

                    try {
                        SimpleDateFormat newformat = new SimpleDateFormat("d MMM");
                        String datestring = trip.getModifieddate().split("T")[0];
                        SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd");
                        String reformattedStr = newformat.format(oldformat.parse(datestring));
                        //holder.tvTripDate.setText(reformattedStr);

                        String a[]=reformattedStr.split(" ");
                        tvDate.setText(a[0]);
                        tvMnth.setText(a[1]);

                        String timestring = trip.getModifieddate().split("T")[1];
                        tvTime.setText(timestring);

                    }catch(ParseException e){e.printStackTrace();}

                    tvRouteName.setText(trip.getRouteName());
                    tvDriverName.setText(trip.getDrivername());
                    tvVehno.setText(trip.getVehicleRegNo());
                    stTripId=trip.getTripid();
                    editor.putString("tripId",stTripId);
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+stTripId);
                    driverMobile=trip.getDrivermobile();

                }
                else {
                    progressDialog.dismiss();
                    //rLayout.setVisibility(View.GONE);
                    tvMyTrips.setText("No Trips");
                    tvMyTrips.setTextSize(15);
                    //tvMyTrips.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<TripPojo>> call, Throwable t) {

                progressDialog.dismiss();
                tvRefresh.setVisibility(View.VISIBLE);
                //rLayout.setVisibility(View.GONE);
                tvMyTrips.setText("No Internet Connection !");
                tvMyTrips.setTextSize(12);
                //Toast.makeText(getActivity(),"Please check Internet Connection!",Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void reportingMeans(final String vehicleType, String tripId, String stRepDate)
    {
        JsonObject v=new JsonObject();
        v.addProperty("companyid",companyId);
        v.addProperty("tripid",tripId);
        v.addProperty("empid",empId);
        v.addProperty("vehusedtype",vehicleType);
        v.addProperty("Repdate",stRepDate);
        Call<Pojo> call=REST_CLIENT.sendReportingStatus(v);
        call.enqueue(new Callback<Pojo>() {
            @Override
            public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                if(response.isSuccessful())
                {

                    alertDialog.dismiss();

                    if(vehicleType.equals("Own"))
                    {
                        tvOfficeVeh.setClickable(true);
                        tvOwnVeh.setTextColor(Color.parseColor("#ffffff"));
                        tvOwnVeh.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_accent_solid_nc_bg));

                        tvOfficeVeh.setTextColor(Color.parseColor("#39a799"));
                        tvOfficeVeh.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_accent_stroke_nc_single_bg));

                    }
                    else {

                        tvOwnVeh.setTextColor(Color.parseColor("#39a799"));
                        tvOwnVeh.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_accent_stroke_nc_single_bg));

                        tvOfficeVeh.setTextColor(Color.parseColor("#ffffff"));
                        tvOfficeVeh.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_accent_solid_nc_bg));
                    }

                    Toast.makeText(getActivity(),"Request sent!",Toast.LENGTH_SHORT).show();

                    Intent i=new Intent(getActivity(), ReportingHistoryActivity.class);
                    startActivity(i);
                    getActivity().finish();

                    getEmpTrips();
                }
                else {
                    alertDialog.dismiss();
                    Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pojo> call, Throwable t) {

                alertDialog.dismiss();

                Toast.makeText(getActivity(),"Please check Internet Connection!",Toast.LENGTH_SHORT).show();

            }
        });
    }
    
    
    
}
