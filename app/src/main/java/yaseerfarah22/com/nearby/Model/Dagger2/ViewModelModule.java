package yaseerfarah22.com.nearby.Model.Dagger2;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import yaseerfarah22.com.nearby.Model.Data.FoursquarApi;
import yaseerfarah22.com.nearby.Model.Data.SharedPreferencesMethod;
import yaseerfarah22.com.nearby.Model.ViewModel.VenuesViewModel;
import yaseerfarah22.com.nearby.Model.ViewModel.ViewModelFactory;



@Module
public class ViewModelModule {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }



    @Singleton
    @Provides
    ViewModelFactory viewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap) {

        return new ViewModelFactory(providerMap);
    }

    @Provides
    @Singleton
    @IntoMap
    @ViewModelKey(VenuesViewModel.class)
    ViewModel productViewModel(Context context, FoursquarApi foursquarApi, SharedPreferencesMethod sharedPreferencesMethod) {
        return new VenuesViewModel(context,foursquarApi,sharedPreferencesMethod);
    }




}
