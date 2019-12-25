package yaseerfarah22.com.nearby.Model.Data;

import android.Manifest;
import android.arch.lifecycle.MediatorLiveData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import es.dmoral.toasty.Toasty;
import yaseerfarah22.com.nearby.Model.POJO.MaybeLocation;


/**
 * Created by DELL on 12/22/2019.
 */

public class LocationLiveData extends MediatorLiveData<MaybeLocation> {

    private final long UPDATE_INTERVAL = 5 * 1000;  /* 5 secs */
    private final long FASTEST_INTERVAL = 2000; /* 2 sec */

    private final FusedLocationProviderClient mFusedLocationClient;
    private Context context;
    private LocationRequest mLocationRequest;
    private boolean realTime;
    private LocationCallback mLocationCallback;
    private LocationManager locationManager;

    public LocationLiveData(Context context,boolean realTime) {
        this.context = context;
        this.realTime=realTime;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        locationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        mLocationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult!=null){
                    postValue(new MaybeLocation(true,locationResult.getLastLocation()));
                }else {
                    postValue(new MaybeLocation(false,null));
                }

            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                if (!checkPermission()) {
                    postValue(new MaybeLocation(false,null));
                }
            }
        };

    }


    @Override
    protected void onActive() {
        super.onActive();
        onActiveLocation(realTime);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        stopRealTimeLocation();
    }






    private void realTimeUpdateLocation(){

        mLocationRequest=LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }


    private void stopRealTimeLocation(){
        if (mFusedLocationClient!=null&&realTime){
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    private void getLastLocation(){
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location!=null){
                    postValue(new MaybeLocation(true,location));
                }else {
                    postValue(new MaybeLocation(false,null));

                }
                    }
            ).addOnFailureListener((var1)->{
                Toasty.error(context,"false").show();
                postValue(new MaybeLocation(false,null));

            });
        }

    }


    public void setOnRealTimeLocation(boolean realTime){
           onActiveLocation(realTime);

    }



    public void onActiveLocation( boolean isRealTime ){
        if   (checkPermission()){
            if (isRealTime){
                //realTime update location
                realTimeUpdateLocation();
            }else {
                // get Last Location
                stopRealTimeLocation();
                getLastLocation();
            }

        }else {
            postValue(new MaybeLocation(false,null));
        }

        this.realTime=isRealTime;
    }



    private boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED&&mFusedLocationClient!=null&&locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            return true;
        }
        return false;
    }

}
