package yaseerfarah22.com.nearby.View;


import android.arch.lifecycle.ViewModelProviders;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.TransitionDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.gturedi.views.StatefulLayout;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;

import dagger.android.AndroidInjection;
import es.dmoral.toasty.Toasty;
import yaseerfarah22.com.nearby.Adapter.VenuesCardViewAdapter;
import yaseerfarah22.com.nearby.Model.POJO.Venue;
import yaseerfarah22.com.nearby.R;
import yaseerfarah22.com.nearby.Util.GPSReceiver;
import yaseerfarah22.com.nearby.Util.NetworkReceiver;
import yaseerfarah22.com.nearby.Model.ViewModel.VenuesViewModel;
import yaseerfarah22.com.nearby.Model.ViewModel.ViewModelFactory;
import yaseerfarah22.com.nearby.Util.StateFulScreen;

import static yaseerfarah22.com.nearby.Constants.maxDistance;


public class MainActivity extends AppCompatActivity {
    @Inject
    ViewModelFactory viewModelFactory;

   private VenuesViewModel venuesViewModel;

    private final int REQUEST_CODE = 200;

   private Toolbar toolbar;
    private StatefulLayout statefulLayout;
   private RecyclerView venuesRecycler;
   private RelativeLayout realTimeBtn;
    private TransitionDrawable realTimeTransition;

    private NetworkReceiver networkReceiver;
    private GPSReceiver gpsReceiver;
    private StateFulScreen stateFulScreen;

    private VenuesCardViewAdapter venuesCardViewAdapter;
    private android.location.Location currentLocation=null;

    private boolean isRealTime=true;

    private List<Venue> currentVenues=new ArrayList<>();


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
        stateFulScreen=new StateFulScreen(this);

        networkReceiver=new NetworkReceiver(stateFulScreen);
        gpsReceiver=new GPSReceiver(stateFulScreen);


        venuesRecycler.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        venuesRecycler.setAdapter(venuesCardViewAdapter);


        realTimeBtn.setOnClickListener(view -> {
            venuesViewModel.switchRealTmeLocation();

        });

    }



    @Override
    protected void onStart() {
      stateFulScreen.showLoading();

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

                    stateFulScreen.showContent();
                    currentVenues.clear();
                    currentVenues.addAll(maybeVenues.getVenueList());
                    venuesCardViewAdapter.updateVenuesList(maybeVenues.getVenueList());

                }else {
                    stateFulScreen.noDataFound();
                }

            }else {
                stateFulScreen.somethingWrong();
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

    public StatefulLayout getStatefulLayout(){
        return statefulLayout;
    }
    public Location getCurrentLocation(){
        return currentLocation;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==this.REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                stateFulScreen.showLoading();
                venuesViewModel.reActiveLocation();
            }
        }
    }


    public void getVenues(android.location.Location location){
     if (location!=null){
            if (currentLocation==location&&currentVenues.size()>0){
                stateFulScreen.showContent();
            }else{
                if (stateFulScreen.isOnline()) {
                    if (!stateFulScreen.getIsLoading){
                   stateFulScreen.showLoading();}
                    venuesViewModel.getNearVenues(location.getLatitude() + "," + location.getLongitude());
                }else {
                    stateFulScreen.networkFailed();
                }
            }
        }else {
            stateFulScreen.showLoading();
        }
    }
}
