package com.hjsoft.emptransportstaff.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hjsoft.emptransportstaff.R;
import com.hjsoft.emptransportstaff.model.Pojo;
import com.hjsoft.emptransportstaff.model.RescheduleData;
import com.hjsoft.emptransportstaff.webservices.API;
import com.hjsoft.emptransportstaff.webservices.RestClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoft on 20/6/17.
 */
public class RescheduleEditActivity extends AppCompatActivity {

    DatePicker dp;
    TimePicker tp;
    Button ok;
    ImageView cancel;
    int day,mnth,yr,hr,min;
    String stDate="-",stTime="-",stPlace="Work",stComments;
    TextView tvRepDate,tvRepTime,tvWork,tvHome,tvSave,tvMyProfile;
    EditText etComments;
    String stEmpId;
    Bundle b;
    API REST_CLIENT;
    ImageView ivRepDate,ivRepTime;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "sp";
    int pos;
    ArrayList<RescheduleData> dList;
    RescheduleData d;
    String companyId="CMP0001";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_edit);

        tvRepDate=(TextView)findViewById(R.id.are_tv_rep_date);
        tvRepTime=(TextView)findViewById(R.id.are_tv_rep_time);
        tvWork=(TextView)findViewById(R.id.are_tv_work);
        tvHome=(TextView)findViewById(R.id.are_tv_home);
        etComments=(EditText)findViewById(R.id.are_et_comments);
        tvSave=(TextView)findViewById(R.id.are_tv_submit);
        ivRepDate=(ImageView)findViewById(R.id.are_iv_rep_date);
        ivRepTime=(ImageView)findViewById(R.id.are_iv_rep_time);
        tvMyProfile=(TextView)findViewById(R.id.are_tv_my_profile);

        REST_CLIENT= RestClient.get();

        b=getIntent().getExtras();
        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        stEmpId=pref.getString("empId",null);
        pos=b.getInt("position");
        dList=(ArrayList<RescheduleData>) getIntent().getExtras().getSerializable("list");
        d=dList.get(pos);

        stDate=d.getRepDate().split("T")[0];
        tvRepDate.setText(stDate);
        stTime=d.getRepTime();
        //tvRepTime.setText(d.getRepTime());
        DecimalFormat formatter = new DecimalFormat("00.00");
        String aFormatted = formatter.format(Double.parseDouble(d.getRepTime()));
        tvRepTime.setText(aFormatted);
        if(d.getRepFrom().equals("Work"))
        {
            tvWork.setTextColor(Color.parseColor("#39a799"));
            tvWork.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_stroke_accent_bg));

            tvHome.setTextColor(Color.parseColor("#9e9e9e"));
            tvHome.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_ash_stroke_single_line));
            stPlace="Work";

        }
        else {
            tvHome.setTextColor(Color.parseColor("#39a799"));
            tvHome.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_stroke_accent_bg));

            tvWork.setTextColor(Color.parseColor("#9e9e9e"));
            tvWork.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_ash_stroke_single_line));
            stPlace="Home";

        }

        tvWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stPlace="Work";

                tvWork.setTextColor(Color.parseColor("#39a799"));
                tvWork.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_stroke_accent_bg));

                tvHome.setTextColor(Color.parseColor("#9e9e9e"));
                tvHome.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_ash_stroke_single_line));

            }
        });

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stPlace="Home";

                tvHome.setTextColor(Color.parseColor("#39a799"));
                tvHome.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_stroke_accent_bg));

                tvWork.setTextColor(Color.parseColor("#9e9e9e"));
                tvWork.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_ash_stroke_single_line));

            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stComments=etComments.getText().toString().trim();
                rescheduleTrip();
            }
        });

        ivRepDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDateSettings();
            }
        });

        ivRepTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showTimeSettings();
            }
        });

        tvMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(RescheduleEditActivity.this,RescheduleHistoryActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    public void showDateSettings()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RescheduleEditActivity.this);

        LayoutInflater inflater =getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_date, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
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

                //stDate=day+"-"+mnth+"-"+yr;
                stDate=mnth+"/"+day+"/"+yr;
                tvRepDate.setText(stDate);
                alertDialog.dismiss();
                showTimeSettings();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });


    }


    public void showTimeSettings()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RescheduleEditActivity.this);

        LayoutInflater inflater =getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_time, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        tp = (TimePicker) dialogView.findViewById(R.id.simpleTimePicker);
        ok = (Button) dialogView.findViewById(R.id.at_bt_ok);


        hr = tp.getCurrentHour();
        min = tp.getCurrentMinute();

        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {


//
//         if(i<=hour_cal)
//                {
//                    hr=hour_cal+1;
//                    min=i1;
//                }
//                else {
//                    hr = i;
//                    min = i1 + 1;
//                }

                hr=i;
                min=i1;

                //  System.out.println("time is "+i+"::"+i1);

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date d = makeDateGMT(yr, mnth-1, day);
                Date d1 = new Date();


                if (d.equals(d1)) {

                    Calendar datetime = Calendar.getInstance();
                    Calendar c = Calendar.getInstance();
                    datetime.set(Calendar.HOUR_OF_DAY, hr);
                    datetime.set(Calendar.MINUTE, min);

                    //System.out.println(" *********** Time " + datetime.getTimeInMillis() + ":::" + c.getTimeInMillis());
                    if (datetime.getTimeInMillis() > c.getTimeInMillis()) {

                       stTime = hr + "." + min;
                        //tvRepTime.setText(stTime);

                        if(min<=9)
                        {
                            DecimalFormat formatter = new DecimalFormat("00");
                            String a1=formatter.format(hr);
                            String a2=formatter.format(min);
                            tvRepTime.setText(a1+"."+a2);
                            stTime=a1+"."+a2;
                        }
                        else {
                            DecimalFormat formatter = new DecimalFormat("00.00");
                            String aFormatted = formatter.format(Double.parseDouble(stTime));
                            tvRepTime.setText(aFormatted);
                            stTime=aFormatted;
                        }

                        alertDialog.dismiss();
                        //showPackageDetails();

                    } else {
//            it's before current'
                        Toast.makeText(RescheduleEditActivity.this, "Please Choose Time ahead of current time", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // stDate=day+"/"+mnth+"/"+yr;
                    //stDate = day+"-"+mnth+"-"+yr;
                    stTime = hr + "." + min;
                    //tvRepTime.setText(stTime);

                    if(min<=9)
                    {
                        DecimalFormat formatter = new DecimalFormat("00");
                        String a1=formatter.format(hr);
                        String a2=formatter.format(min);
                        tvRepTime.setText(a1+"."+a2);
                        stTime=a1+"."+a2;

                    }
                    else {
                        DecimalFormat formatter = new DecimalFormat("00.00");
                        String aFormatted = formatter.format(Double.parseDouble(stTime));
                        tvRepTime.setText(aFormatted);
                        stTime=aFormatted;
                    }


                    alertDialog.dismiss();

                    //  showPackageDetails();
                }

            }
        });
    }

    private Date makeDateGMT(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar();
        // calendar.setTimeZone(TimeZone.getTimeZone());
        calendar.set(year,month, day);
        return calendar.getTime();
    }


    public void rescheduleTrip()
    {

        d=dList.get(pos);

        if(stDate.equals("-")||stTime.equals("-"))
        {
            Toast.makeText(RescheduleEditActivity.this,"Please enter Date/Time !",Toast.LENGTH_SHORT).show();

        }
        else {

            JsonObject v = new JsonObject();
            v.addProperty("reqid",d.getReqId());
            v.addProperty("empid", stEmpId);
            v.addProperty("Repdate",stDate);
            v.addProperty("RepTime",stTime);
            v.addProperty("RepFrom",stPlace);
            v.addProperty("companyid",companyId);

            System.out.println(stEmpId+":"+stDate+":"+stTime+":"+stPlace+":"+stComments);

            Call<Pojo> call = REST_CLIENT.updateReschedule(v);
            call.enqueue(new Callback<Pojo>() {
                @Override
                public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                    if (response.isSuccessful()) {
                        tvSave.setVisibility(View.GONE);
                        ivRepDate.setClickable(false);
                        ivRepTime.setClickable(false);
                        tvHome.setClickable(false);
                        tvWork.setClickable(false);
                        etComments.setClickable(false);
                        etComments.setEnabled(false);
                        Toast.makeText(RescheduleEditActivity.this,"Trip Updated !",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(RescheduleEditActivity.this,RescheduleHistoryActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {
//                        tvSave.setVisibility(View.GONE);
//                        ivRepDate.setClickable(false);
//                        ivRepTime.setClickable(false);
//                        tvHome.setClickable(false);
//                        tvWork.setClickable(false);
//                        etComments.setClickable(false);
//                        etComments.setEnabled(false);
                        Toast.makeText(RescheduleEditActivity.this,"Request already exist for the specified date !",Toast.LENGTH_LONG).show();
//                        Intent i=new Intent(RescheduleActivity.this,TripSummaryActivity.class);
//                        startActivity(i);
//                        finish();

                        tvRepDate.setText("DD-MM-YYYY");
                        tvRepTime.setText("HH:MM:SS");
                        etComments.setText(" ");


                    }
                }

                @Override
                public void onFailure(Call<Pojo> call, Throwable t) {

                    Toast.makeText(RescheduleEditActivity.this,"No Internet Connection!",Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i=new Intent(RescheduleEditActivity.this,RescheduleHistoryActivity.class);
        startActivity(i);
        finish();
    }
}
