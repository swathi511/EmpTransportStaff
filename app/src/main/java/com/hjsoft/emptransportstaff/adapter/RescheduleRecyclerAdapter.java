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

import com.hjsoft.emptransportstaff.R;
import com.hjsoft.emptransportstaff.model.ReportData;
import com.hjsoft.emptransportstaff.model.RescheduleData;
import com.hjsoft.emptransportstaff.model.ReschedulePojo;
import com.hjsoft.emptransportstaff.webservices.API;
import com.hjsoft.emptransportstaff.webservices.RestClient;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by hjsoft on 19/6/17.
 */
public class RescheduleRecyclerAdapter extends RecyclerView.Adapter<RescheduleRecyclerAdapter.MyViewHolder>  {

    ArrayList<RescheduleData> customArrayList;
    Context context;
    RescheduleData data;
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
    private AdapterCallback mAdapterCallback;


    public RescheduleRecyclerAdapter(Context context, ArrayList<RescheduleData> customArrayList, String stTripId)
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
        try {
            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reschedule_details, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        data=customArrayList.get(position);

        String d[]=data.getRepDate().split("T");

        holder.lLayout.setTag(position);
        holder.tvStatus.setVisibility(View.GONE);
        holder.tvEdit.setVisibility(View.GONE);
        holder.lLayout.setBackgroundColor(Color.parseColor("#e9e7e7"));
        holder.lLayout.setClickable(true);
        //holder.tvDateTime.setText(d[0]);

        try {

            SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy");
            String datestring = data.getRepDate().split("T")[0];
            SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd");
            String reformattedStr = newformat.format(oldformat.parse(datestring));
            holder.tvDateTime.setText(reformattedStr);
        }
        catch (ParseException e){e.printStackTrace();

            holder.tvDateTime.setText(d[0]);
        }
        //holder.tvTime.setText(data.getRepTime());
        DecimalFormat formatter = new DecimalFormat("00.00");
        String aFormatted = formatter.format(Double.parseDouble(data.getRepTime()));
        holder.tvTime.setText(aFormatted);
        holder.tvLocation.setText(data.getRepFrom());

        if(data.getTravelDeskStatus().equals("For Acceptance"))
        {
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setText("Pending Approval");
            holder.tvEdit.setVisibility(View.VISIBLE);
           // holder.lLayout.setClickable(true);
        }
        else {
            //holder.tvStatus.setText("Approved");

            holder.lLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.lLayout.setClickable(false);
        }

        holder.lLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos;
                try {
                    pos= (int) holder.lLayout.getTag();
                    //Toast.makeText(context,pos+":::",Toast.LENGTH_SHORT).show();
                    mAdapterCallback.onMethodCallback(pos,customArrayList);
                }
                catch (ClassCastException e)
                {
                    e.printStackTrace();
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return customArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDateTime,tvLocation,tvStatus,tvEdit,tvTime;
        LinearLayout lLayout;

        public MyViewHolder(final View itemView) {

            super(itemView);
            tvDateTime=(TextView)itemView.findViewById(R.id.resh_tv_date);
            tvLocation=(TextView)itemView.findViewById(R.id.resh_tv_location);
            tvStatus=(TextView)itemView.findViewById(R.id.resh_tv_status);
            tvEdit=(TextView)itemView.findViewById(R.id.resh_tv_edit);
            lLayout=(LinearLayout)itemView.findViewById(R.id.resh_lLayout);
            tvTime=(TextView)itemView.findViewById(R.id.resh_tv_time);
        }

    }

    public static interface AdapterCallback {
        void onMethodCallback(int position, ArrayList<RescheduleData> data);
    }

}
