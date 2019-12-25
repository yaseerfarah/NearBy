package yaseerfarah22.com.nearby.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import yaseerfarah22.com.nearby.Model.Interface.GPSConnection;
import yaseerfarah22.com.nearby.Model.Interface.NetworkConnection;


public class GPSReceiver extends BroadcastReceiver {


    GPSConnection gpsConnection;


    public GPSReceiver(GPSConnection gpsConnection) {
        this.gpsConnection = gpsConnection;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

         if(intent.getAction().trim().matches(LocationManager.PROVIDERS_CHANGED_ACTION)){

                gpsConnection.noGPS();


        }





    }
}
