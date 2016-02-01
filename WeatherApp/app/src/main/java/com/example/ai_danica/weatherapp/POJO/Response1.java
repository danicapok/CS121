package com.example.ai_danica.weatherapp.POJO;

/**
 * Created by Ai_Danica on 1/28/2016.
 */
//import javax.annotation.Generated;
import com.example.ai_danica.weatherapp.POJO.Conditions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Generated("org.jsonschema2pojo")
public class Response1 {

    @SerializedName("conditions")
    @Expose
    public Conditions conditions;
    @SerializedName("result")
    @Expose
    public String result;

}
