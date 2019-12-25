package yaseerfarah22.com.nearby.Model.Interface;

/**
 * Created by DELL on 12/23/2019.
 */

public interface ScreenState {



    void showContent();
    void showLoading();
    void networkFailed();
    void somethingWrong();
    void noGPS();
    void noDataFound();

}
