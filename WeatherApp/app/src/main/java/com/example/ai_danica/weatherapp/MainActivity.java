package com.example.ai_danica.weatherapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;





public class MainActivity extends AppCompatActivity {

    Conditions c;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //printText();
    }

    public void clickedButton(View v){
        getURL();
    }

    private void getURL (){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://luca-teaching.appspot.com/")	//We are using Foursquare API to get data
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .client(httpClient)	//add logging
                .build();

        WeatherService service = retrofit.create(WeatherService.class);

        final Call<Result> queryResponseCall =
                service.registerUser(c, result);

        //Call retrofit asynchronously
        queryResponseCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Response<Result> response) {
                System.out.println("The result is: " + response.body());
                String temperature = response.body().result;
                System.out.println(temperature);

            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
            }
        });


    }


    public interface WeatherService {
        @GET("weather/default/get_weather")
        Call<Result> registerUser(@Query("Conditions") Conditions c,
                                                @Query("result") String result);
    }

    private void testing(){
        Conditions c = new Conditions();


    }

    private void printText(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String s = prefs.getString("myprefs", null);
        Gson gson = new Gson();

        Conditions c = gson.fromJson(s, Conditions.class);
        System.out.println(c.observationLocation.city);
    }
}
