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
import com.hjsoft.emptransportstaff.adapter.RescheduleRecyclerAdapter;
import com.hjsoft.emptransportstaff.model.RescheduleData;
import com.hjsoft.emptransportstaff.model.ReschedulePojo;
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
public class RescheduleHistoryFragment extends Fragment {
    
    View v;
    RecyclerView rView;
    API REST_CLIENT;
    String stEmpId;
    ArrayList<RescheduleData> repData=new ArrayList<>();
    RescheduleRecyclerAdapter mAdapter;
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

        v = inflater.inflate(R.layout.activity_reschedule_history, container, false);

        rView=(RecyclerView)v.findViewById(R.id.arqh_rv_list);
        ivRefresh=(ImageView)v.findViewById(R.id.arqh_iv_refresh);

        REST_CLIENT= RestClient.get();

        pref = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        stEmpId =pref.getString("empId",null);

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        getAllReschedulingRequests();

        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                repData.clear();
                progressDialog.show();

                getAllReschedulingRequests();
            }
        });
        
        return  v;

    }

    public void getAllReschedulingRequests()
    {
        Call<List<ReschedulePojo>> call=REST_CLIENT.getRescheduleHistory("CMP0001",stEmpId);
        call.enqueue(new Callback<List<ReschedulePojo>>() {
            @Override
            public void onResponse(Call<List<ReschedulePojo>> call, Response<List<ReschedulePojo>> response) {

                List<ReschedulePojo> repPojoList;
                ReschedulePojo repPojo;

                if(response.isSuccessful())
                {
                    repPojoList=response.body();

                    for(int i=0;i<repPojoList.size();i++)
                    {
                        repPojo=repPojoList.get(i);

                        repData.add(new RescheduleData(repPojo.getCompanyid(),repPojo.getEmpid(),repPojo.getReqid(),repPojo.getReqdate(),
                                repPojo.getRepdate(),repPojo.getReptime(),repPojo.getRemarks(),repPojo.getRepFrom(),repPojo.getTraveldeskstatus(),
                                repPojo.getAllocationstatus(),repPojo.getApprovedby(),repPojo.getApproveddate()));
                    }
                }

                if(repData.size()!=0) {

                    Collections.reverse(repData);

                    progressDialog.dismiss();
                    mAdapter = new RescheduleRecyclerAdapter(getActivity(), repData, stTripId);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    rView.setLayoutManager(mLayoutManager);
                    rView.setItemAnimator(new DefaultItemAnimator());
                    rView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<ReschedulePojo>> call, Throwable t) {

                progressDialog.dismiss();

                Toast.makeText(getActivity(),"Please check Internet connection!",Toast.LENGTH_SHORT).show();

            }
        });
    }
}
