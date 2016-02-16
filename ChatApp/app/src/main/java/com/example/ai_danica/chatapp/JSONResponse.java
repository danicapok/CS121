package com.example.ai_danica.chatapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ai_Danica on 2/12/2016.
 */
import java.util.ArrayList;
import java.util.List;
//import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class JSONResponse {

    @SerializedName("result_list")
    @Expose
    public List<RegistrationResponse> resultList = new ArrayList<RegistrationResponse>();
    @SerializedName("result")
    @Expose
    public String result;

}
