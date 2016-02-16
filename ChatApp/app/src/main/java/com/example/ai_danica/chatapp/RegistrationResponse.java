package com.example.ai_danica.chatapp;

/**
 * Created by Ai_Danica on 2/9/2016.
 */
//import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Generated("org.jsonschema2pojo")
public class RegistrationResponse {

    @SerializedName("timestamp")
    @Expose
    public String timestamp;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("nickname")
    @Expose
    public String nickname;
    @SerializedName("user_id")
    @Expose
    public String userId;

}