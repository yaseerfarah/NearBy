package yaseerfarah22.com.nearby.Model.POJO;


import android.location.*;

/**
 * Created by DELL on 12/22/2019.
 */

public class MaybeLocation {

    private boolean isLocation;
    private android.location.Location location;

    public MaybeLocation(boolean isLocation, android.location.Location location) {
        this.isLocation = isLocation;
        this.location = location;
    }

    public boolean isLocation() {
        return isLocation;
    }

    public void setLocation(boolean location) {
        isLocation = location;
    }

    public android.location.Location getLocation() {
        return location;
    }

    public void setLocation(android.location.Location location) {
        this.location = location;
    }
}
