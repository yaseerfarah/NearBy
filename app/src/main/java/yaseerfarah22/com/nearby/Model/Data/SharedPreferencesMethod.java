package yaseerfarah22.com.nearby.Model.Data;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Inject;




public class SharedPreferencesMethod {


    private SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesMethod(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }


    public void setBoolean(String name,boolean b){

        sharedPreferences.edit().putBoolean(name,b).apply();

    }


    public boolean getBoolean(String name){

        return sharedPreferences.getBoolean(name,true);
    }



    public void setString(String name,String value){

        sharedPreferences.edit().putString(name,value).apply();

    }



    public String getString(String name){

        return sharedPreferences.getString(name,"");
    }




}
