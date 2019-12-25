package yaseerfarah22.com.nearby.Model.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import yaseerfarah22.com.nearby.Model.Data.FoursquarApi;
import yaseerfarah22.com.nearby.Model.Data.LocationLiveData;
import yaseerfarah22.com.nearby.Model.Data.SharedPreferencesMethod;

import yaseerfarah22.com.nearby.Model.POJO.FoursquareResponse;
import yaseerfarah22.com.nearby.Model.POJO.GroupsItem;
import yaseerfarah22.com.nearby.Model.POJO.ImageResponse;
import yaseerfarah22.com.nearby.Model.POJO.MaybeLocation;
import yaseerfarah22.com.nearby.Model.POJO.MaybeVenues;
import yaseerfarah22.com.nearby.Model.POJO.Response;
import yaseerfarah22.com.nearby.Model.POJO.Venue;

import static yaseerfarah22.com.nearby.Constants.*;

/**
 * Created by DELL on 12/22/2019.
 */

public class VenuesViewModel extends ViewModel {


    private final String REAL_TIME_KEY="RealTime";

    private MediatorLiveData<MaybeVenues> venuesListLiveData;
    private MediatorLiveData<Boolean> isRealTimeLiveData;
    private Context context;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private LocationLiveData locationLiveData;
    private FoursquarApi foursquarApi;
    private List<Venue> venueList;
    private SharedPreferencesMethod sharedPreferencesMethod;
    private boolean realTimeLocation;


    @Inject
    public VenuesViewModel(Context context, FoursquarApi foursquarApi, SharedPreferencesMethod sharedPreferencesMethod) {
        this.context = context;
        this.foursquarApi = foursquarApi;
        this.sharedPreferencesMethod = sharedPreferencesMethod;

        this.venuesListLiveData=new MediatorLiveData<>();
        this.isRealTimeLiveData=new MediatorLiveData<>();
        this.venueList=new ArrayList<>();
        this.realTimeLocation=getRealTimeLocation();
        this.locationLiveData=new LocationLiveData(context,realTimeLocation);
        this.isRealTimeLiveData.postValue(realTimeLocation);



    }


    private boolean getRealTimeLocation(){
        return sharedPreferencesMethod.getBoolean(REAL_TIME_KEY);
    }

    public LiveData<MaybeVenues> getVenuesLiveData(){
        return venuesListLiveData;
    }

    public MediatorLiveData<MaybeLocation> getLocationLiveData(){
        return locationLiveData;
    }

    public LiveData<Boolean> getIsRealtimeLiveData(){
        return isRealTimeLiveData;
    }

    public void switchRealTmeLocation(){

        realTimeLocation=!realTimeLocation;
        sharedPreferencesMethod.setBoolean(REAL_TIME_KEY,realTimeLocation);
        locationLiveData.setOnRealTimeLocation(realTimeLocation);
        isRealTimeLiveData.postValue(realTimeLocation);

    }


    public void reActiveLocation(){

        locationLiveData.onActiveLocation(realTimeLocation);
    }


    public void getNearVenues(String latlng) {

        disposables.add(foursquarApi.getVenues(latlng, Client_ID, Client_Secret, apiVersion).subscribeOn(Schedulers.io()).
                map(retrofit2.Response::body)
                .map(FoursquareResponse::getResponse)
                .map(Response::getGroups)
                .flatMap(Observable::fromIterable)
                .map(GroupsItem::getItems)
                .flatMap(Observable::fromIterable)
                //.flatMap(itemsItem -> getVenuePhotoObservable(itemsItem.getVenue()))
                .map(itemsItem -> itemsItem.getVenue())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNextVenue, this::onError, this::onCompleteVenues));


    }



    private Observable<Venue> getVenuePhotoObservable(Venue venue) {

        Observable<Venue>  getPhotoObservable =  foursquarApi.getPhoto(venue.getId(),Client_ID,Client_Secret,apiVersion).
                subscribeOn(Schedulers.io()).
                map(retrofit2.Response::body)
                .map(ImageResponse::getResponse)
                .map(response -> response.getPhotos())
                .map(photos -> photos.getItems())
                .flatMap(Observable::fromIterable)
                .take(1)
                .map(itemsItem ->
                {

                    venue.setImgUrl(itemsItem.getPrefix() + "612x612" + itemsItem.getSuffix());

                    return venue;


                });


        return  getPhotoObservable;
    }




    private void onNextVenue(Venue venue){
        venueList.add(venue);
    }
    private void onCompleteVenues() {


        venuesListLiveData.postValue(new MaybeVenues(true,venueList));
    }
    private void onError(Throwable throwable) {
        Toasty.warning(context,String.valueOf(throwable.getMessage()), Toast.LENGTH_LONG).show();

        venuesListLiveData.postValue(new MaybeVenues(false,null));
    }




    @Override
    protected void onCleared() {
        super.onCleared();

        disposables.clear();

    }
}
