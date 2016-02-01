package com.example.ai_danica.weatherapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ai_danica.weatherapp.POJO.Conditions;
import com.example.ai_danica.weatherapp.POJO.Example;
import com.example.ai_danica.weatherapp.POJO.Response1;
import com.google.gson.Gson;

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

    TextView city;
    TextView temperature;
    TextView humidity;
    TextView wind;
    TextView weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = (TextView) findViewById(R.id.city);
        temperature = (TextView) findViewById(R.id.temperature);
        humidity = (TextView) findViewById(R.id.humidity);
        wind = (TextView) findViewById(R.id.wind);
        weather = (TextView) findViewById(R.id.weather);


    }

    public void clickedButton(View v){
        getURL(v);
    }

    private void getURL (View v){
        final View viewer = v;

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

        Call<Example> test = service.getResponse();
        //Call retrofit asynchronously
       test.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Response<Example> response) {
                System.out.println("The result is: " + response.body());
                //System.out.println("" + response.body().response.conditions.observationLocation.city);
                if(response.body().response.result.equals("error")){
                    Toast.makeText(MainActivity.this, "Error Message: Cannot display temperature. Try again!",
                            Toast.LENGTH_LONG).show();
                }
                else printConditions(response, viewer);

            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "this is my Toast message!!! =)",
                        Toast.LENGTH_LONG).show();
                // Log error here since request failed
            }
        });


    }

    public void printConditions(Response<Example> response, View v){
        String location = response.body().response.conditions.observationLocation.city;
        String elevation = response.body().response.conditions.observationLocation.elevation;
        Double temp_f = response.body().response.conditions.tempF;
        city.setText("City: " + location + " , elevation: " + elevation);
        temperature.setText("Temperature: " + temp_f);


    }


    public interface WeatherService {
        @GET("weather/default/get_weather")
         //Response1 response (@Query("Response") Response1 resp);
        Call<Example> getResponse();
        //Call<Response1> registerUser(@Query("Conditions") Conditions c,  @Query("response") String response);
    }


}
