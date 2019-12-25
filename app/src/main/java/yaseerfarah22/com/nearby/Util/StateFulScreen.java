package yaseerfarah22.com.nearby.Util;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.gturedi.views.CustomStateOptions;
import com.gturedi.views.StatefulLayout;

import java.lang.ref.WeakReference;

import es.dmoral.toasty.Toasty;
import yaseerfarah22.com.nearby.Model.Interface.ScreenState;
import yaseerfarah22.com.nearby.Model.Interface.GPSConnection;
import yaseerfarah22.com.nearby.Model.Interface.NetworkConnection;
import yaseerfarah22.com.nearby.R;
import yaseerfarah22.com.nearby.View.MainActivity;

/**
 * Created by DELL on 12/25/2019.
 */

public class StateFulScreen implements ScreenState,NetworkConnection,GPSConnection {

    private final int REQUEST_CODE = 200;

    private CustomStateOptions networkCustom=new CustomStateOptions().image(R.drawable.ic_cloud_off_black_24dp);
    private CustomStateOptions noDataCustom=new CustomStateOptions().image(R.drawable.ic_error_outline_black_24dp);
    private CustomStateOptions noGPSCustom=new CustomStateOptions().image(R.drawable.ic_gps_off_black_24dp);


    WeakReference<MainActivity> activityWeakReference;
    private LocationManager locationManager;

    private String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private boolean isOnline=true;
    private boolean isLoading=false;

    public StateFulScreen(MainActivity activityWeakReference) {
        this.activityWeakReference = new WeakReference<MainActivity>(activityWeakReference);
        this.locationManager=(LocationManager) activityWeakReference.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void setIsOnline(boolean isOnline) {
        this.isOnline=isOnline;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public boolean getIsLoading;

    @Override
    public void showContent() {
        if (activityWeakReference.get()!=null){
            MainActivity mainActivity=activityWeakReference.get();
            StatefulLayout statefulLayout=mainActivity.getStatefulLayout();
            statefulLayout.showContent();
            isLoading=false;

        }
    }

    @Override
    public void showLoading() {
        if (activityWeakReference.get()!=null){
            MainActivity mainActivity=activityWeakReference.get();
            StatefulLayout statefulLayout=mainActivity.getStatefulLayout();
            if (!isLoading) {
                statefulLayout.showLoading("");
                isLoading = true;
            }
        }

    }



    @Override
    public void networkFailed() {
        if (activityWeakReference.get()!=null){
            MainActivity mainActivity=activityWeakReference.get();
            StatefulLayout statefulLayout=mainActivity.getStatefulLayout();

            statefulLayout.showCustom(networkCustom.message("Oooopss...  Check your Connection")
                    .buttonText("Try Again")
                    .buttonClickListener(view -> {
                        if (isOnline){
                            mainActivity.getVenues(mainActivity.getCurrentLocation());
                        }else {
                            Toasty.warning(mainActivity,"Check your Connection").show();
                        }
                    }));
            isLoading=false;

        }

    }

    @Override
    public void somethingWrong() {
        if (activityWeakReference.get()!=null){
            MainActivity mainActivity=activityWeakReference.get();
            StatefulLayout statefulLayout=mainActivity.getStatefulLayout();

            statefulLayout.showCustom(networkCustom.message("something went wrong ...")
                    .buttonText("Try Again")
                    .buttonClickListener(view -> mainActivity.getVenues(mainActivity.getCurrentLocation())));

            isLoading=false;

        }

    }

    @Override
    public void noGPS() {
        if (activityWeakReference.get()!=null){
            MainActivity mainActivity=activityWeakReference.get();
            StatefulLayout statefulLayout=mainActivity.getStatefulLayout();

            if (!checkGPS()){

                statefulLayout.showCustom(noGPSCustom.message("Oooopss...  Check your GPS")
                        .buttonText("Try Again")
                        .buttonClickListener(view ->  checkGPS()));

                isLoading=false;
            }

        }

    }

    @Override
    public void noDataFound() {
        if (activityWeakReference.get()!=null){
            MainActivity mainActivity=activityWeakReference.get();
            StatefulLayout statefulLayout=mainActivity.getStatefulLayout();

            statefulLayout.showCustom(noDataCustom.message("No Data Found"));
            isLoading=false;

        }

    }



    public boolean checkGPS() {

        if (activityWeakReference.get() != null) {

            MainActivity mainActivity=activityWeakReference.get();

            boolean permissionCheck1 = ContextCompat.checkSelfPermission(mainActivity, reqPermissions[0]) ==
                    PackageManager.PERMISSION_GRANTED;
            boolean permissionCheck2 = ContextCompat.checkSelfPermission(mainActivity, reqPermissions[1]) ==
                    PackageManager.PERMISSION_GRANTED;

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                buildAlertMessageNoGps();
                return false;
            } else if (!(permissionCheck1 && permissionCheck2)) {
                // If permissions are not already granted, request permission from the user.

                ActivityCompat.requestPermissions(mainActivity, reqPermissions, REQUEST_CODE);
                return false;
            }

            return true;
        }

        return false;

    }



    private void buildAlertMessageNoGps() {
        if (activityWeakReference.get() != null) {

            MainActivity mainActivity=activityWeakReference.get();

            final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        dialogInterface.cancel();
                       mainActivity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    })

                    .setNegativeButton("No", (dialogInterface, i) -> {
                        dialogInterface.cancel();

                    });

            final AlertDialog alert = builder.create();
            alert.show();
        }

    }


}
