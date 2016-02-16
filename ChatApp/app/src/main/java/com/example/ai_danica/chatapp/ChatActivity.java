package com.example.ai_danica.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ai_Danica on 2/8/2016.
 */
public class ChatActivity extends AppCompatActivity {


    private LocationData locationData = LocationData.getLocationData();

    boolean you = false;
    EditText messageText;
    String msg;
    Button sentbtn;
    Button refreshbtn;
    ArrayList<ListElement> aList;
    MyAdapter aa;
    String nickname;
    String user_id;
    float longitude;
    float latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatactivity);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = prefs.getString("user_id", null);
        //nickname = prefs.getString("nickname", null);



        //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        //System.out.println(locationData.getLocation().getLatitude());
        nickname = getIntent().getExtras().getString("nickname");
        longitude = (float) locationData.getLocation().getLongitude();
        latitude = (float) locationData.getLocation().getLatitude();
        sentbtn = (Button) findViewById(R.id.send);
        refreshbtn = (Button) findViewById(R.id.refresh);
        messageText = (EditText) findViewById(R.id.message);

//        distinguishMessages();
        aList = new ArrayList<ListElement>();
        aa = new MyAdapter(this, R.layout.list_element, aList);
        ListView myListView = (ListView) findViewById(R.id.listView);

        distinguishMessages();
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();
        get();

        distinguishMessages();
    }

    @Override
    protected void onResume(){
        distinguishMessages();
        aList = new ArrayList<ListElement>();
        aa = new MyAdapter(this, R.layout.list_element, aList);
        ListView myListView = (ListView) findViewById(R.id.listView);
        distinguishMessages();
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();

        get();

        super.onResume();
    }

    public void sendClicked(View v){
        distinguishMessages();
        System.out.println(user_id);
        post();
        messageText.setText("");
    }

    public void refreshClicked(View v){
        distinguishMessages();
        get();
    }

    private void distinguishMessages(){
        Log.d("Test", user_id + "in here");
        for(int i = 0; i < aList.size(); i++){
            if(aList.get(i).user_id.equals(user_id)){
                you = true;
            }
        }
    }

    private void post(){

        msg = messageText.getText().toString();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://luca-teaching.appspot.com/localmessages/default/")
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .client(httpClient)	//add logging
                .build();

        themessages service = retrofit.create(themessages.class);
        //distinguishMessages();
        aList.add(new ListElement(msg, user_id, "timestamp", nickname, you));
        aa.notifyDataSetChanged();
        Call<RegistrationResponse> getResponse = service.post_message(user_id, nickname, msg, "message_id", latitude, longitude);

//        Call retrofit asynchronously
        getResponse.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Response<RegistrationResponse> response) {
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
            }
        });
    }



    private void get(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://luca-teaching.appspot.com/localmessages/default/")
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .client(httpClient)	//add logging
                .build();

        themessages service = retrofit.create(themessages.class);
        Call<JSONResponse> getResponse = service.get_message(user_id, latitude, longitude);

        //Call retrofit asynchronously
        getResponse.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Response<JSONResponse> response) {
                List<RegistrationResponse> venues;
                if(response.code() == 200)
                {
                    venues = response.body().resultList;
                }
                else
                    venues = new ArrayList<RegistrationResponse>();
                aList.clear();
                distinguishMessages();
                //Display the messages from last refreshed.
                Log.d("here", "time" + response.body().resultList.size() );
                int i = venues.size() - 1;
                while(i >= 0) {
                    String textMessage = response.body().resultList.get(i).message;
                    String user_id = response.body().resultList.get(i).userId;
                    distinguishMessages();
                    String timestamp = response.body().resultList.get(i).timestamp;
                    String nick = response.body().resultList.get(i).nickname;

                    aList.add(new ListElement(textMessage, user_id, timestamp, nick, you));
                    i--;

                }
                aa.notifyDataSetChanged();


            }


            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
            }
        });


    }

    public interface themessages{
        @POST("post_message")
        Call<RegistrationResponse> post_message(@Query("user_id") String user_id,
                                            @Query("nickname") String nickname,
                                            @Query("message") String message,
                                            @Query("message_id") String message_id,
                                            @Query("lat") float lat,
                                            @Query("lng") float lon);
        @GET("get_messages")
        Call<JSONResponse> get_message(@Query("user_id") String user_id,
                                       @Query("lat") float lat,
                                       @Query("lng") float lon);

    }


}
