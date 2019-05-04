package com.hjsoft.emptransportstaff.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hjsoft.emptransportstaff.R;
import com.hjsoft.emptransportstaff.model.Pojo;
import com.hjsoft.emptransportstaff.model.ReportData;
import com.hjsoft.emptransportstaff.webservices.API;
import com.hjsoft.emptransportstaff.webservices.RestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoft on 19/6/17.
 */
public class ReportingRecyclerAdapter extends RecyclerView.Adapter<ReportingRecyclerAdapter.MyViewHolder>  {

    ArrayList<ReportData> customArrayList;
    Context context;
    ReportData data;
    LayoutInflater inflater;
    boolean accept=false;
    API REST_CLIENT;
    Dialog dialog;
    int pos;
    boolean status=false;
    String empId;
    String stTripId;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "sp";
    String companyId="CMP0001";


    public ReportingRecyclerAdapter(Context context, ArrayList<ReportData> customArrayList,String stTripId)
    {

        this.context=context;
        this.customArrayList=customArrayList;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog=new Dialog(context);
        this.stTripId=stTripId;
        REST_CLIENT= RestClient.get();
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        empId=pref.getString("empId",null);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reporting_history, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        System.out.println("***************************************"+position);

        data=customArrayList.get(position);
        holder.lLayout.setTag(position);
        holder.tvCompanyVehicle.setEnabled(false);
        holder.tvOwnVehicle.setEnabled(false);
        holder.tvCompanyVehicle.setVisibility(View.VISIBLE);
        holder.tvOwnVehicle.setVisibility(View.VISIBLE);
        holder.lLayout.setBackgroundColor(Color.parseColor("#e9e7e7"));
        holder.tvApproval.setVisibility(View.GONE);

        String dt[]=data.getRepDate().split("T");

        try {

            SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy");
            String datestring = data.getRepDate().split("T")[0];
            SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd");
            String reformattedStr = newformat.format(oldformat.parse(datestring));
            holder.tvDate.setText(reformattedStr);
        }
        catch (ParseException e){e.printStackTrace();

            holder.tvDate.setText(dt[0]);
        }

        if(data.getVehUsedType().equals("Company"))
        {
            holder.tvOwnVehicle.setTextColor(Color.parseColor("#d0d0d0"));
            holder.tvOwnVehicle.setBackgroundResource(R.drawable.rect_ash_stroke_nc_single_line);
            holder.tvCompanyVehicle.setTextColor(Color.parseColor("#39a799"));
            holder.tvCompanyVehicle.setBackgroundResource(R.drawable.rect_accent_stroke_nc_single_bg);

            if(data.getStatus().equals("Accepted"))
            {

                holder.lLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.tvOwnVehicle.setVisibility(View.GONE);
            }
            else {

                holder.tvOwnVehicle.setEnabled(true);
                holder.tvApproval.setVisibility(View.VISIBLE);
            }
        }
        else {

            holder.tvCompanyVehicle.setTextColor(Color.parseColor("#d0d0d0"));
            holder.tvCompanyVehicle.setBackgroundResource(R.drawable.rect_ash_stroke_nc_single_line);
            holder.tvOwnVehicle.setTextColor(Color.parseColor("#39a799"));
            holder.tvOwnVehicle.setBackgroundResource(R.drawable.rect_accent_stroke_nc_single_bg);

            if(data.getStatus().equals("Accepted"))
            {

                holder.lLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.tvCompanyVehicle.setVisibility(View.GONE);
            }
            else {

                holder.tvCompanyVehicle.setEnabled(true);
                holder.tvApproval.setVisibility(View.VISIBLE);
            }

        }


        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = df.format(c.getTime());

        if(todayDate.equals(dt[0]))
        {

        }
        else {
            stTripId="-";
        }


        holder.tvCompanyVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos=(int)holder.lLayout.getTag();

                final ReportData r;
                r=customArrayList.get(pos);



                JsonObject v=new JsonObject();
                v.addProperty("repid",r.getRepId());
                v.addProperty("vehusedtype","Company");
                v.addProperty("companyid",companyId);
                Call<Pojo> call=REST_CLIENT.updateReportingStatus(v);
                call.enqueue(new Callback<Pojo>() {
                    @Override
                    public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                        if(response.isSuccessful())
                        {
                            holder.tvOwnVehicle.setTextColor(Color.parseColor("#d0d0d0"));
                            holder.tvOwnVehicle.setBackgroundResource(R.drawable.rect_ash_stroke_nc_single_line);
                            holder.tvCompanyVehicle.setTextColor(Color.parseColor("#39a799"));
                            holder.tvCompanyVehicle.setBackgroundResource(R.drawable.rect_accent_stroke_nc_single_bg);

                            if(r.getStatus().equals("Accepted"))
                            {

                            }
                            else {

                                holder.tvOwnVehicle.setEnabled(true);
                                holder.tvApproval.setVisibility(View.VISIBLE);
                            }


                        }
                        else {

                            Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Pojo> call, Throwable t) {

                        Toast.makeText(context,"Please check Internet connection!",Toast.LENGTH_LONG).show();


                    }
                });

            }
        });

        holder.tvOwnVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos=(int)holder.lLayout.getTag();

                final ReportData r;
                r=customArrayList.get(pos);

                JsonObject v=new JsonObject();
                v.addProperty("repid",r.getRepId());
                v.addProperty("vehusedtype","Own");
                v.addProperty("companyid",companyId);
                Call<Pojo> call=REST_CLIENT.updateReportingStatus(v);
                call.enqueue(new Callback<Pojo>() {
                    @Override
                    public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                        if(response.isSuccessful())
                        {
                            holder.tvCompanyVehicle.setTextColor(Color.parseColor("#d0d0d0"));
                            holder.tvCompanyVehicle.setBackgroundResource(R.drawable.rect_ash_stroke_nc_single_line);
                            holder.tvOwnVehicle.setTextColor(Color.parseColor("#39a799"));
                            holder.tvOwnVehicle.setBackgroundResource(R.drawable.rect_accent_stroke_nc_single_bg);

                            if(r.getStatus().equals("Accepted"))
                            {

                            }
                            else {

                                holder.tvCompanyVehicle.setEnabled(true);
                                holder.tvApproval.setVisibility(View.VISIBLE);
                            }
                        }
                        else {

                            Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Pojo> call, Throwable t) {

                        Toast.makeText(context,"Please check Internet connection!",Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return customArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCompanyVehicle,tvOwnVehicle,tvApproval;
        TextView tvDate;
        LinearLayout lLayout;

        public MyViewHolder(final View itemView) {
            super(itemView);

            tvDate=(TextView)itemView.findViewById(R.id.reph_tv_date);
            tvCompanyVehicle=(TextView)itemView.findViewById(R.id.reph_tv_company_vehicle);
            tvOwnVehicle=(TextView)itemView.findViewById(R.id.reph_tv_own_vehicle);
            lLayout=(LinearLayout)itemView.findViewById(R.id.reph_lLayout);
            tvApproval=(TextView)itemView.findViewById(R.id.reph_tv_approval);


        }

    }

    /*public void reportingMeans(final String vehicleType, String tripId, String stRepDate)
    {
        JsonObject v=new JsonObject();
        v.addProperty("companyid","CMP0001");
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

                    if(vehicleType.equals("Company"))
                    {
                        holder.tvOwnVehicle.setTextColor(Color.parseColor("#d0d0d0"));
                        holder.tvOwnVehicle.setBackgroundResource(R.drawable.rect_ash_stroke_nc_single_line);
                        holder.tvCompanyVehicle.setTextColor(Color.parseColor("#39a799"));
                        holder.tvCompanyVehicle.setBackgroundResource(R.drawable.rect_accent_stroke_nc_single_bg);

                        if(data.getStatus().equals("Accepted"))
                        {

                        }
                        else {

                            holder.tvOwnVehicle.setEnabled(true);
                            holder.tvApproval.setVisibility(View.VISIBLE);
                        }
                    }
                    else {

                        holder.tvCompanyVehicle.setTextColor(Color.parseColor("#d0d0d0"));
                        holder.tvCompanyVehicle.setBackgroundResource(R.drawable.rect_ash_stroke_nc_single_line);
                        holder.tvOwnVehicle.setTextColor(Color.parseColor("#39a799"));
                        holder.tvOwnVehicle.setBackgroundResource(R.drawable.rect_accent_stroke_nc_single_bg);

                        if(data.getStatus().equals("Accepted"))
                        {

                        }
                        else {

                            holder.tvCompanyVehicle.setEnabled(true);
                            holder.tvApproval.setVisibility(View.VISIBLE);
                        }

                    }

                }
                else {

                }
            }

            @Override
            public void onFailure(Call<Pojo> call, Throwable t) {


            }
        });
    }*/

}
