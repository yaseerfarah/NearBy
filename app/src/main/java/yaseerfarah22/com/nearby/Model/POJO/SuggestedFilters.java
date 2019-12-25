package yaseerfarah22.com.nearby.Model.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 12/21/2019.
 */

public class SuggestedFilters {
    @SerializedName("header")
    private String header;
    @SerializedName("filters")
    private List<Filters> filters;

    public void setHeader(String header){
        this.header = header;
    }
    public String getHeader(){
        return this.header;
    }
    public void setFilters(List<Filters> filters){
        this.filters = filters;
    }
    public List<Filters> getFilters(){
        return this.filters;
    }

}
