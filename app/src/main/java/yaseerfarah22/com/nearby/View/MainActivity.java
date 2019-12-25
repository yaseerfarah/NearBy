package yaseerfarah22.com.nearby.View;


import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.TransitionDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.gturedi.views.CustomStateOptions;
import com.gturedi.views.StatefulLayout;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;

import dagger.android.AndroidInjection;
import es.dmoral.toasty.Toasty;
import yaseerfarah22.com.nearby.Adapter.VenuesCardViewAdapter;
import yaseerfarah22.com.nearby.Model.Interface.EmptyState;
import yaseerfarah22.com.nearby.Model.Interface.GPSConnection;
import yaseerfarah22.com.nearby.Model.Interface.NetworkConnection;
import yaseerfarah22.com.nearby.Model.POJO.Venue;
import yaseerfarah22.com.nearby.R;
import yaseerfarah22.com.nearby.Util.GPSReceiver;
import yaseerfarah22.com.nearby.Util.NetworkReceiver;
import yaseerfarah22.com.nearby.Model.ViewModel.VenuesViewModel;
import yaseerfarah22.com.nearby.Model.ViewModel.ViewModelFactory;

import static yaseerfarah22.com.nearby.Constants.maxDistance;


public class MainActivity extends AppCompatActivity implements NetworkConnection,EmptyState,GPSConnection {


    @Inject
    ViewModelFactory viewModelFactory;

   private VenuesViewModel venuesViewModel;

    private final int REQUEST_CODE = 200;

   private Toolbar toolbar;
    private StatefulLayout statefulLayout;
   private RecyclerView venuesRecycler;
   private RelativeLayout realTimeBtn;
    private TransitionDrawable realTimeTransition;


    private LocationManager locationManager;
    private NetworkReceiver networkReceiver;
    private GPSReceiver gpsReceiver;

    private VenuesCardViewAdapter venuesCardViewAdapter;
    private android.location.Location currentLocation=null;


    private boolean isOnline=true;
    private boolean isRealTime=true;
    private boolean isLoading=true;


    private List<Venue> currentVenues=new ArrayList<>();
    private String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};




    private CustomStateOptions networkCustom=new CustomStateOptions().image(R.drawable.ic_cloud_off_black_24dp);
    private CustomStateOptions noDataCustom=new CustomStateOptions().image(R.drawable.ic_error_outline_black_24dp);

    private CustomStateOptions noGPSCustom=new CustomStateOptions().image(R.drawable.ic_gps_off_black_24dp);







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidInjection.inject(this);
        venuesViewModel= ViewModelProviders.of(this,viewModelFactory).get(VenuesViewModel.class);

        toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        statefulLayout=(StatefulLayout) findViewById(R.id.stateful);
        venuesRecycler=(RecyclerView)findViewById(R.id.near_list);
        realTimeBtn=(RelativeLayout) findViewById(R.id.reltime_btn);

        venuesCardViewAdapter=new VenuesCardViewAdapter(this);
        realTimeTransition=(TransitionDrawable) realTimeBtn.getBackground() ;

        networkReceiver=new NetworkReceiver(this);
        gpsReceiver=new GPSReceiver(this);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);


        venuesRecycler.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        venuesRecycler.setAdapter(venuesCardViewAdapter);


        realTimeBtn.setOnClickListener(view -> {
            venuesViewModel.switchRealTmeLocation();

        });



    }



    @Override
    protected void onStart() {


        statefulLayout.showLoading("");
        isLoading=true;
        venuesViewModel.getLocationLiveData().observe(this,maybeLocation -> {

            if (maybeLocation.isLocation()) {

                if (maybeLocation.getLocation() != null) {

                    Toasty.warning(MainActivity.this, String.valueOf(maybeLocation.getLocation().getLatitude() + "  , " + maybeLocation.getLocation().getLongitude())).show();
                    if (currentLocation == null) {
                        currentLocation = maybeLocation.getLocation();
                        getVenues(currentLocation);
                    } else {
                        if (currentLocation.distanceTo(maybeLocation.getLocation()) >= maxDistance) {
                            Toasty.warning(MainActivity.this, "MAX").show();

                            currentLocation = maybeLocation.getLocation();
                            getVenues(currentLocation);
                        }
                    }

                }
            }

        });


        venuesViewModel.getVenuesLiveData().observe(this,maybeVenues -> {

            if (maybeVenues.isVenues()){

                if (maybeVenues.getVenueList().size()>0){

                    // Toasty.warning(MainActivity.this, String.valueOf(maybeVenues.getVenueList().size())).show();

                    statefulLayout.showContent();
                    isLoading=false;
                    currentVenues.clear();
                    currentVenues.addAll(maybeVenues.getVenueList());
                    venuesCardViewAdapter.updateVenuesList(maybeVenues.getVenueList());

                }else {
                    noDataFound();
                }

            }else {
                somethingWrong();
            }

        });


        venuesViewModel.getIsRealtimeLiveData().observe(this,aBoolean -> {

            if (aBoolean!=isRealTime){

                if (isRealTime){
                    realTimeTransition.startTransition(200);
                }else {
                    realTimeTransition.reverseTransition(200);
                }
                isRealTime=aBoolean;
            }

        });


        IntentFilter netFilter=new IntentFilter();
        netFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkReceiver,netFilter);


        IntentFilter gpsFilter=new IntentFilter();
        gpsFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(gpsReceiver,gpsFilter);




        super.onStart();


    }


    @Override
    protected void onStop() {


        venuesViewModel.getLocationLiveData().removeObservers(this);
        venuesViewModel.getVenuesLiveData().removeObservers(this);
        venuesViewModel.getIsRealtimeLiveData().removeObservers(this);

        unregisterReceiver(networkReceiver);
        unregisterReceiver(gpsReceiver);

        venuesCardViewAdapter.updateVenuesList(new ArrayList<>());


        super.onStop();

    }








    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean checkGPS(){


        boolean permissionCheck1 = ContextCompat.checkSelfPermission(MainActivity.this, reqPermissions[0]) ==
                PackageManager.PERMISSION_GRANTED;
        boolean permissionCheck2 = ContextCompat.checkSelfPermission(MainActivity.this, reqPermissions[1]) ==
                PackageManager.PERMISSION_GRANTED;

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            buildAlertMessageNoGps();
            return false;
        }

        else if (!(permissionCheck1 && permissionCheck2)) {
            // If permissions are not already granted, request permission from the user.

            ActivityCompat.requestPermissions(MainActivity.this, reqPermissions, REQUEST_CODE);
            return false;
        }

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode==this.REQUEST_CODE){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&&isOnline) {
                statefulLayout.showLoading("");
                isLoading=true;
                venuesViewModel.reActiveLocation();

            }

        }


    }



    @Override
    public void setIsOnline(boolean isOnline) {
        this.isOnline=isOnline;
    }

    @Override
    public void networkFailed(){

        statefulLayout.showCustom(networkCustom.message("Oooopss...  Check your Connection")
                .buttonText("Try Again")
                .buttonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isOnline){
                            getVenues(currentLocation);
                        }else {
                            Toasty.warning(MainActivity.this,"Check your Connection").show();
                        }
                    }
                }));
        isLoading=false;

    }

    @Override
    public void somethingWrong(){

        statefulLayout.showCustom(networkCustom.message("something went wrong ...")
                .buttonText("Try Again")
                .buttonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       getVenues(currentLocation);
                    }
                }));

        isLoading=false;

    }

    @Override
    public void noGPS(){

        if (!checkGPS()){

            statefulLayout.showCustom(noGPSCustom.message("Oooopss...  Check your GPS")
                    .buttonText("Try Again")
                    .buttonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkGPS();
                        }
                    }));

            isLoading=false;
        }

    }

    @Override
    public void noDataFound(){

        statefulLayout.showCustom(noDataCustom.message("No Data Found"));
        isLoading=false;

    }


    private void getVenues(android.location.Location location){

     if (location!=null){

            if (currentLocation==location&&currentVenues.size()>0){
                statefulLayout.showContent();
            }else{

                if (isOnline) {
                    if (!isLoading){
                    statefulLayout.showLoading("");}
                    venuesViewModel.getNearVenues(location.getLatitude() + "," + location.getLongitude());
                }else {
                    networkFailed();
                }
            }

        }else {
            statefulLayout.showLoading("");
            isLoading=true;
        }
    }


}
