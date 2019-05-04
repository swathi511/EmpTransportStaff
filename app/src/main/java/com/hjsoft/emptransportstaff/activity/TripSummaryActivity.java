package com.hjsoft.emptransportstaff.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.hjsoft.emptransportstaff.adapter.DrawerItemCustomAdapter;
import com.hjsoft.emptransportstaff.fragments.RescheduleHistoryFragment;
import com.hjsoft.emptransportstaff.fragments.TripSummaryFragment;
import com.hjsoft.emptransportstaff.model.NavigationData;
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
 * Created by hjsoft on 12/6/17.
 */
public class TripSummaryActivity extends AppCompatActivity {

    TextView tvStaffName, tvOfficeVeh, tvOwnVeh;
    TextView tvMyTrips, tvRouteName, tvDate, tvMnth, tvTime, tvDriverName, tvVehno;
    API REST_CLIENT;
    String uname, pwd, companyCode = "CMP0001";
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
    int day, mnth, yr;
    String stSelectedTrip, stTripId;
    TextView tvReschedule;
    TextView tvTrackRide, tvCallDriver;
    String driverMobile;
    String companyId = "CMP0001";
    ProgressDialog progressDialog;
    String empId;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "sp";
    ImageView ivLogout;
    String stDate;
    AlertDialog alertDialog;
    TextView tvRefresh;
    AlertDialog.Builder dialogBuilder;
    View dialogView;
    TextView tvTodayDate;
    ImageView ivReportingHistory;

    //////////////////////////////

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    DrawerItemCustomAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        NavigationData[] drawerItem = new NavigationData[4];

        drawerItem[0] = new NavigationData(R.drawable.arrow, "My Profile");
        drawerItem[1] = new NavigationData(R.drawable.arrow, "Reporting Requests");
        drawerItem[2] = new NavigationData(R.drawable.arrow, "Reschedule Requests");
        drawerItem[3] = new NavigationData(R.drawable.arrow, "Logout");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setupDrawerToggle();

        Fragment fragment = new TripSummaryFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, fragment, "all_duties").commit();
        setTitle("My Profile");
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;
        adapter.setSelectedItem(position);

        switch (position) {
            case 0:
                // fragment = new TrackCabsFragment();
                break;
            case 1:
                // System.out.println("doing nothing.....");
                Intent i=new Intent(TripSummaryActivity.this,ReportingHistoryActivity.class);
                startActivity(i);
                finish();

                break;
           /* case 2:
                Intent k=new Intent(this,ProfileActivity.class);
                startActivity(k);
                finish();
                break;*/
            case 2:

                Intent j=new Intent(TripSummaryActivity.this,RescheduleHistoryActivity.class);
                startActivity(j);
                finish();

                break;
            case 3:
                SessionManager s = new SessionManager(getApplicationContext());
                s.logoutUser();
                Intent l = new Intent(this, MainActivity.class);
                l.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                l.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(l);
                finish();

                break;

            default:
                break;
        }

        if (fragment != null) {

            openFragment(fragment, position);

        } else {
            // Log.e("MainActivity", "Error in creating fragment");
        }
    }

    private void openFragment(Fragment fragment, int position) {

        Fragment containerFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);

        if (containerFragment.getClass().getName().equalsIgnoreCase(fragment.getClass().getName())) {
            mDrawerLayout.closeDrawer(mDrawerList);
            return;
        } else {
            /*
           FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            */
            mDrawerLayout.closeDrawer(mDrawerList);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
        //  getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" +mTitle + "</font>")));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        /*
        int titleId = getResources().getIdentifier("toolbar", "id", "android");
        TextView abTitle = (TextView) findViewById(titleId);
        abTitle.setTextColor(Color.parseColor("#000000"));*/
    }

    void setupDrawerToggle() {
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }
}