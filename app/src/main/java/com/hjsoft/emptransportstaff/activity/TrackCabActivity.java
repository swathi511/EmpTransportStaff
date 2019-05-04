package com.hjsoft.emptransportstaff.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hjsoft.emptransportstaff.R;
import com.hjsoft.emptransportstaff.model.LatLngPojo;
import com.hjsoft.emptransportstaff.webservices.API;
import com.hjsoft.emptransportstaff.webservices.RestClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoft on 13/6/17.
 */
public class TrackCabActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    SupportMapFragment mapFragment;
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected boolean mRequestingLocationUpdates;
    boolean btStartState;
    final static int REQUEST_LOCATION = 199;
    protected Location mLastLocation;
    double latitude,longitude,current_lat,current_long;
    Geocoder geocoder;
    List<Address> addresses;
    LatLng lastLoc,curntloc;
    String complete_address;
    float[] results=new float[3];
    long res=0;
    boolean entered=false;
    Marker mPickup,mDrop;
    TextView tvCurrentLoc,tvBack;
    boolean isMarkerRotating=false;
    LatLng startPosition,finalPosition,currentPosition,lastLocDist;
    double cpLat,cpLng;
    SimpleDateFormat dateFormat;
    String timeUpdated;
    Handler h,hDist;
    Runnable r,rDist;
    float inAccurate = 10;
    Marker car;
    API REST_CLIENT;
    String stTripId;
    List<LatLngPojo> dataList;
    LatLngPojo data;
    Bundle b;
    boolean first=true;
    String companyId="CMP0001";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_cab);

        tvCurrentLoc=(TextView)findViewById(R.id.atc_tv_place);
        tvBack=(TextView)findViewById(R.id.atc_tv_back);

        b=getIntent().getExtras();
        stTripId=b.getString("tripId");

        REST_CLIENT= RestClient.get();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        geocoder = new Geocoder(this, Locale.getDefault());

        if(Build.VERSION.SDK_INT<23)
        {
            //System.out.println("Sdk_int is"+Build.VERSION.SDK_INT);
            //System.out.println("the enetred values is "+entered);
            establishConnection();
        }
        else
        {
            if(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                establishConnection();
            }
            else
            {
                if(shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    Toast.makeText(TrackCabActivity.this,"Location Permission is required for this app to run!",Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            }
        }

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //finish();
                Intent i=new Intent(TrackCabActivity.this,TripSummaryActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    public void establishConnection(){

        b=getIntent().getExtras();
        stTripId=b.getString("tripId");

        buildGoogleApiClient();
        buildLocationSettingsRequest();
        //gettingLocationUpdates();
        entered=true;
    }


    @Override
    protected void onStart() {

        super.onStart();

        if(Build.VERSION.SDK_INT>=23)
        {
            if(!entered)
            {

            }
            else
            {

                mGoogleApiClient.connect();

            }
        }
        else
        {
            mGoogleApiClient.connect();

        }
    }

    @Override
    protected void onStop() {

        if(mGoogleApiClient!=null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mGoogleApiClient!=null) {
            if (mGoogleApiClient.isConnected()) {
                stopLocationUpdates();

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(entered)
        {
            if(mGoogleApiClient.isConnecting()||mGoogleApiClient.isConnected())
            {

            }
            else {
                mGoogleApiClient.connect();
            }
        }

        if(mGoogleApiClient!=null) {

            if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates && btStartState) {
                startLocationUpdates();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {

        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();
    }

    protected void buildLocationSettingsRequest() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        //Location Settings Satisfied
                        startLocationUpdates();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            status.startResolutionForResult(TrackCabActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to resolve it
                        break;
                }
            }
        });
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(45000);//45 sec
        mLocationRequest.setFastestInterval(30000);//5 sec
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,TrackCabActivity.this);
            mRequestingLocationUpdates=true;
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    protected void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, TrackCabActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                establishConnection();

            } else {
                Toast.makeText(TrackCabActivity.this, "Permission not granted", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mapFragment.getMapAsync(this);
        if (mLastLocation == null) {
            try {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        if(mRequestingLocationUpdates&&btStartState)
        {
            startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        /*



        if(mLastLocation==null)
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            lastLoc = new LatLng(latitude, longitude);
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String add1=addresses.get(0).getAddressLine(1);
                String add2=addresses.get(0).getAddressLine(2);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                complete_address=address+" "+add1+" "+add2;
                tvCurrentLoc.setText(complete_address);
            }
            catch(IOException e)
            {e.printStackTrace();
                complete_address="No response from server";
                tvCurrentLoc.setText(complete_address);
            }
            mMap.addMarker(new MarkerOptions().position(lastLoc)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_image)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, 15));
            mMap.getUiSettings().setMapToolbarEnabled(false);
        }


        if(mLastLocation!=null) {

            current_lat=location.getLatitude();
            current_long=location.getLongitude();
            if (current_lat != 0 && current_long != 0) {
                curntloc = new LatLng(current_lat, current_long);
                Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(), current_lat, current_long, results);
                location.getAccuracy();
                res = res + (long) results[0];

                try {
                    addresses = geocoder.getFromLocation(current_lat, current_long, 1);
                    int l=addresses.get(0).getMaxAddressLineIndex();
                    String add="",add1="",add2="";

                    for(int k=0;k<l;k++)
                    {
                        add=add+addresses.get(0).getAddressLine(k);
                        add=add+" ";

                        if(k==1)
                        {
                            add1=addresses.get(0).getAddressLine(k);
                        }
                        if(k==2)
                        {
                            add2=addresses.get(0).getAddressLine(k);
                        }
                    }
                    String address = addresses.get(0).getAddressLine(0);
                    String add_1=addresses.get(0).getAddressLine(1);//current place name eg:Nagendra nagar,Hyderabad
                    String add_2=addresses.get(0).getAddressLine(2);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    //complete_address=address+" "+add1+" "+add2;
                    tvCurrentLoc.setText(add);
                    complete_address=add;
                }
                catch(IOException e) {
                    e.printStackTrace();
                    complete_address="No response from server";
                    tvCurrentLoc.setText(complete_address);
                }
                mMap.addMarker(new MarkerOptions().position(curntloc)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_image)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curntloc, 15));
                mMap.getUiSettings().setMapToolbarEnabled(false);
            }
        }

        */

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        lastLoc = new LatLng(latitude, longitude);

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(lastLoc)
                .zoom(16)
                //.bearing(30).tilt(45)
                .build()));
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        try{
            addresses = geocoder.getFromLocation(latitude,longitude, 1);
            int l=addresses.get(0).getMaxAddressLineIndex();
            String add="",add1="",add2="";

            for(int k=0;k<l;k++)
            {
                add=add+addresses.get(0).getAddressLine(k);
                add=add+" ";

                if(k==1)
                {
                    add1=addresses.get(0).getAddressLine(k);
                }
                if(k==2)
                {
                    add2=addresses.get(0).getAddressLine(k);
                }
            }
            tvCurrentLoc.setText(add);
        }
        catch (Exception e){e.printStackTrace();}

        if(first) {

            b = getIntent().getExtras();
            stTripId = b.getString("tripId");
            gettingLocationUpdates();
            first=false;
        }

    }

    public void gettingLocationUpdates() {

        h = new Handler();
        r = new Runnable() {
            @Override
            public void run() {

                h.postDelayed(r,30000);
                //currentTime = java.text.DateFormat.getTimeInstance().format(new Date());
                //System.out.println("tripID "+stTripId);

                Call<List<LatLngPojo>> call=REST_CLIENT.getCoordinates(stTripId,companyId);
                call.enqueue(new Callback<List<LatLngPojo>>() {
                    @Override
                    public void onResponse(Call<List<LatLngPojo>> call, Response<List<LatLngPojo>> response) {

                        if(response.isSuccessful()) {
                            dataList = response.body();
                            data = dataList.get(0);

                            if (data.getLatitude().equals("") || data.getLongitude().equals("")) {

                               Toast.makeText(TrackCabActivity.this,"Trip not in progress !",Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(TrackCabActivity.this,TripSummaryActivity.class);
                                startActivity(i);
                                finish();
                            }

                            else
                            {
                                LatLng cabLocLatLng = new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()));

                                if (mMap != null) {
                                    if (car == null) {
                                        car = mMap.addMarker(new MarkerOptions().position(cabLocLatLng)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.cab_icon)));
                                    } else {
                                        //cab.setPosition(cabLocLatLng);

                                        startPosition = car.getPosition();
                                        finalPosition = new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()));
                                        double toRotation = bearingBetweenLocations(startPosition, finalPosition);
                                        rotateMarker(car, (float) toRotation);
                                        accelerateDecelerate();

                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(finalPosition, 16));
                                        mMap.getUiSettings().setMapToolbarEnabled(false);

//                                                    CameraPosition oldPos = mMap.getCameraPosition();
//
//                                                    CameraPosition pos = CameraPosition.builder(oldPos).bearing((float)toRotation).build();
//                                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos));

                                    }
                                }

                                try {
                                    addresses = geocoder.getFromLocation(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()), 1);
                                    if (addresses.size() != 0) {
                                        int l = addresses.get(0).getMaxAddressLineIndex();
                                        String add = "", add1 = "", add2 = "";

                                        for (int k = 0; k < l; k++) {
                                            add = add + addresses.get(0).getAddressLine(k);
                                            add = add + " ";

                                            if (k == 1) {
                                                add1 = addresses.get(0).getAddressLine(k);
                                            }
                                            if (k == 2) {
                                                add2 = addresses.get(0).getAddressLine(k);
                                            }
                                        }
                                        String address = addresses.get(0).getAddressLine(0);
                                        String add_1 = addresses.get(0).getAddressLine(1);//current place name eg:Nagendra nagar,Hyderabad
                                        String add_2 = addresses.get(0).getAddressLine(2);
                                        String city = addresses.get(0).getLocality();
                                        String state = addresses.get(0).getAdminArea();
                                        //complete_address=address+" "+add1+" "+add2;
                                        tvCurrentLoc.setText(add1 + " " + add2);
                                        complete_address = add;
                                    } else {
                                        tvCurrentLoc.setText("-");
                                        complete_address = "-";
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    complete_address = "Unable to get the location details";
                                    tvCurrentLoc.setText(complete_address);
                                }
                            }
                        }
                        else {
                            //System.out.println("************************"+response.message()+":"+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LatLngPojo>> call, Throwable t) {

                    }
                });



            }
        };

        h.post(r);
    }


    public static String getCurrentTime() {
        //date output format
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    private void accelerateDecelerate()
    {
        final Handler handler = new Handler();

        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 5000;
        final boolean hideMarker = false;

        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;

                cpLat=startPosition.latitude * (1 - t) + finalPosition.latitude * t;
                cpLng= startPosition.longitude * (1 - t) + finalPosition.longitude * t;

                currentPosition = new LatLng(cpLat,cpLng);

                car.setPosition(currentPosition);
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,16));

                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        car.setVisible(false);
                    } else {
                        car.setVisible(true);
                    }
                }
            }
        });
    }

    private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    private void rotateMarker(final Marker marker, final float toRotation) {
        if(!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 500;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        //
        //finish();

        Intent i=new Intent(TrackCabActivity.this,TripSummaryActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(h!=null)
        {
            h.removeCallbacks(r);
        }
    }
}




/*


(03:23:48  IST) Cargo Server:
Kumar

9999999999
(03:25:02  IST) Cargo Server:
Srikanth V

9000000123
 */
