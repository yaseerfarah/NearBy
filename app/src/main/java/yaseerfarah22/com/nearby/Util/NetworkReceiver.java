package yaseerfarah22.com.nearby.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import yaseerfarah22.com.nearby.Model.Interface.NetworkConnection;


public class NetworkReceiver extends BroadcastReceiver {


    NetworkConnection networkConnection;


    public NetworkReceiver(NetworkConnection networkConnection) {
        this.networkConnection = networkConnection;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

         if(intent.getAction().trim().matches("android.net.conn.CONNECTIVITY_CHANGE")){

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if (activeNetwork==null){
                this.networkConnection.setIsOnline(false);
                this.networkConnection.networkFailed();


            }else {
                this.networkConnection.setIsOnline(true);
            }



        }


    }
}
