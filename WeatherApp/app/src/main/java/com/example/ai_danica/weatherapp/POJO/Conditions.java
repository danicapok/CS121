package com.example.ai_danica.weatherapp.POJO;

/**
 * Created by Ai_Danica on 1/28/2016.
 */
//import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Generated("org.jsonschema2pojo")
public class Conditions {

    @SerializedName("wind_gust_mph")
    @Expose
    public Integer windGustMph;
    @SerializedName("temp_f")
    @Expose
    public Double tempF;
    @SerializedName("observation_location")
    @Expose
    public ObservationLocation observationLocation;
    @SerializedName("temp_c")
    @Expose
    public Double tempC;
    @SerializedName("relative_humidity")
    @Expose
    public String relativeHumidity;
    @SerializedName("weather")
    @Expose
    public String weather;
    @SerializedName("dewpoint_c")
    @Expose
    public Integer dewpointC;
    @SerializedName("windchill_c")
    @Expose
    public String windchillC;
    @SerializedName("pressure_mb")
    @Expose
    public String pressureMb;
    @SerializedName("windchill_f")
    @Expose
    public String windchillF;
    @SerializedName("dewpoint_f")
    @Expose
    public Integer dewpointF;
    @SerializedName("wind_mph")
    @Expose
    public Double windMph;
}
