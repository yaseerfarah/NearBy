package yaseerfarah22.com.nearby.Model.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 12/21/2019.
 */
public class Filters
{
    @SerializedName("name")
    private String name;
    @SerializedName("key")
    private String key;

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setKey(String key){
        this.key = key;
    }
    public String getKey(){
        return this.key;
    }
}