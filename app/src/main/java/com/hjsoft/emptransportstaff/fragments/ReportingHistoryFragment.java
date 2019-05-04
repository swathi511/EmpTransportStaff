package com.hjsoft.emptransportstaff.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.hjsoft.emptransportstaff.R;
import com.hjsoft.emptransportstaff.adapter.ReportingRecyclerAdapter;
import com.hjsoft.emptransportstaff.model.ReportData;
import com.hjsoft.emptransportstaff.model.ReportPojo;
import com.hjsoft.emptransportstaff.webservices.API;
import com.hjsoft.emptransportstaff.webservices.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoft on 20/6/17.
 */
public class ReportingHistoryFragment extends Fragment {

    View v;
    RecyclerView rView;
    API REST_CLIENT;
    String stEmpId;
    ArrayList<ReportData> repData=new ArrayList<>();
    ReportingRecyclerAdapter mAdapter;
    String stTripId;
    ImageView ivRefresh;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "sp";
    ProgressDialog progressDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_reporting_history, container, false);

        rView=(RecyclerView)v.findViewById(R.id.arh_rv_list);
        ivRefresh=(ImageView)v.findViewById(R.id.arh_iv_refresh);

        REST_CLIENT= RestClient.get();
//        stTripId=getActivity().getIntent().getExtras().getString("tripId");

        pref = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        stEmpId =pref.getString("empId",null);
        stTripId=pref.getString("tripId",null);

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        getAllReportingRequests();

        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repData.clear();

                progressDialog.show();

                getAllReportingRequests();
            }
        });

        return v;
    }

    public void getAllReportingRequests()
    {
        Call<List<ReportPojo>> call=REST_CLIENT.getReportingHistory("CMP0001",stEmpId);
        call.enqueue(new Callback<List<ReportPojo>>() {
            @Override
            public void onResponse(Call<List<ReportPojo>> call, Response<List<ReportPojo>> response) {

                List<ReportPojo> repPojoList;
                ReportPojo repPojo;

                if(response.isSuccessful())
                {

                    repPojoList=response.body();

                    for(int i=0;i<repPojoList.size();i++)
                    {
                        repPojo=repPojoList.get(i);

                        repData.add(new ReportData(repPojo.getEmpid(),repPojo.getReqdate(),repPojo.getRepId(),repPojo.getRepdate(),
                                repPojo.getReptime(),repPojo.getVehusedtype(),repPojo.getRoutename(),repPojo.getTraveldeskstatus(),
                                repPojo.getApprovedby(),repPojo.getApproveddate()));

                    }

                }

                if(repData.size()!=0) {

                    Collections.reverse(repData);

                    progressDialog.dismiss();
                    mAdapter = new ReportingRecyclerAdapter(getActivity(), repData, stTripId);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    rView.setLayoutManager(mLayoutManager);
                    rView.setItemAnimator(new DefaultItemAnimator());
                    rView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                else {

                    Toast.makeText(getActivity(),"No data!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReportPojo>> call, Throwable t) {

                progressDialog.dismiss();

                Toast.makeText(getActivity(),"Please check Internet connection!",Toast.LENGTH_SHORT).show();

            }
        });
    }

}
