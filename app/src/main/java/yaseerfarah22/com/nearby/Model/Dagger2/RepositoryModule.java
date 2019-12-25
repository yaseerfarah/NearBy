package yaseerfarah22.com.nearby.Model.Dagger2;


import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import yaseerfarah22.com.nearby.Model.Data.FoursquarApi;
import yaseerfarah22.com.nearby.Model.Data.SharedPreferencesMethod;

import static yaseerfarah22.com.nearby.Constants.BASE_URL;



@Module
public class RepositoryModule {





    @Provides
    @Singleton
    public SharedPreferences sharedPreferences(Context context){

        return context.getSharedPreferences("LocationInfo",Context.MODE_PRIVATE);
    }



    @Provides
    @Singleton
    public SharedPreferencesMethod sharedPreferencesMethod(SharedPreferences sharedPreferences){

       return new SharedPreferencesMethod(sharedPreferences);

    }




    @Provides
    @Singleton
    public FoursquarApi foursquarApi(Retrofit retrofit){

        return retrofit.create(FoursquarApi.class);

    }

    @Singleton
    @Provides
    public Retrofit retrofit(){

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


}
