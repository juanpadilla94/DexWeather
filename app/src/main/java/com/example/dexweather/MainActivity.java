package com.example.dexweather;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    // API KEY: 0b3a33b127b30991aad586510ac20441
    // https://api.darksky.net/forecast/0b3a33b127b30991aad586510ac20441/37.8267,-122.4233
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String apiKey = "0b3a33b127b30991aad586510ac20441";
        double latitude = 32.7157;
        double longitude = -117.1611;
        String url = "https://api.darksky.net/forecast/"
                + apiKey + "/"
                + String.valueOf(latitude) + ","
                + String.valueOf(longitude);
        try {
            String jsonGetResult = new WeatherModel().execute(url).get();
            System.out.println(jsonGetResult);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
