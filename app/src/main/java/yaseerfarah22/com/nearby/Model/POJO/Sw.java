package yaseerfarah22.com.nearby.Model.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 12/21/2019.
 */

public class Sw {

    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;

    public void setLat(double lat){
        this.lat = lat;
    }
    public double getLat(){
        return this.lat;
    }
    public void setLng(double lng){
        this.lng = lng;
    }
    public double getLng(){
        return this.lng;
    }
}
