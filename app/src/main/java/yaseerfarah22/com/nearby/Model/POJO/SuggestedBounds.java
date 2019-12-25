package yaseerfarah22.com.nearby.Model.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 12/21/2019.
 */

public class SuggestedBounds {

    @SerializedName("ne")
    private Ne ne;
    @SerializedName("sw")
    private Sw sw;

    public void setNe(Ne ne){
        this.ne = ne;
    }
    public Ne getNe(){
        return this.ne;
    }
    public void setSw(Sw sw){
        this.sw = sw;
    }
    public Sw getSw(){
        return this.sw;
    }


}
