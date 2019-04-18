package com.example.dexweather;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            String daily = new JSONObject(jsonGetResult).get("daily").toString();
            String data = new JSONObject(daily).get("data").toString();
            JSONArray jsonArr = new JSONArray(data);
            for(int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonHolder = new JSONObject(jsonArr.get(i).toString());
                System.out.println("summary: " + jsonHolder.get("summary").toString());
                System.out.println("temp high: " + jsonHolder.get("apparentTemperatureHigh").toString());
                System.out.println("temp low: " + jsonHolder.get("apparentTemperatureLow").toString());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
