package yaseerfarah22.com.nearby.Model.POJO;

import java.util.List;

/**
 * Created by DELL on 12/22/2019.
 */

public class MaybeVenues {

    private boolean isVenues;
    private List<Venue> venueList;

    public MaybeVenues(boolean isVenues, List<Venue> venueList) {
        this.isVenues = isVenues;
        this.venueList = venueList;
    }


    public boolean isVenues() {
        return isVenues;
    }

    public void setVenues(boolean venues) {
        isVenues = venues;
    }

    public List<Venue> getVenueList() {
        return venueList;
    }

    public void setVenueList(List<Venue> venueList) {
        this.venueList = venueList;
    }
}
