package yaseerfarah22.com.nearby.Model.Data;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import yaseerfarah22.com.nearby.Model.POJO.FoursquareResponse;
import yaseerfarah22.com.nearby.Model.POJO.ImageResponse;

/**
 * Created by DELL on 12/22/2019.
 */

public interface FoursquarApi {

    @GET("venues/explore/")
    Observable<Response<FoursquareResponse>> getVenues(@Query("ll") String latlng, @Query("client_id") String client_id,
                                                       @Query("client_secret") String client_secret, @Query("v") String v);
    @GET("venues/{id}/photos")
    Observable<Response<ImageResponse>> getPhoto(@Path("id") String id , @Query("client_id") String client_id,
                                                 @Query("client_secret") String client_secret, @Query("v") String v);


}
