package yaseerfarah22.com.nearby.Model.Dagger2;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import yaseerfarah22.com.nearby.View.MainActivity;



@Module
public abstract class ActivityBuilder {


    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();






}
