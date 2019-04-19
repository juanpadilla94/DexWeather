package com.example.dexweather;

import android.graphics.Color;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    boolean colorFlip;

    // API KEY: 0b3a33b127b30991aad586510ac20441
    // https://api.darksky.net/forecast/0b3a33b127b30991aad586510ac20441/37.8267,-122.4233
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWeather();
    }

    private void getWeather() {
        String apiKey = "0b3a33b127b30991aad586510ac20441";
        double latitude = 32.7157;
        double longitude = -117.1611;
        String url = "https://api.darksky.net/forecast/"
                + apiKey + "/"
                + latitude + ","
                + longitude;
        try {
            String jsonGetResult = new WeatherModel().execute(url).get();
            String daily = new JSONObject(jsonGetResult).get("daily").toString();
            String data = new JSONObject(daily).get("data").toString();
            JSONArray jsonArr = new JSONArray(data);
            colorFlip = true;
            for(int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonHolder = new JSONObject(jsonArr.get(i).toString());
                String summary = jsonHolder.get("summary").toString();
                String tempHigh = jsonHolder.get("apparentTemperatureHigh").toString();
                String tempLow = jsonHolder.get("apparentTemperatureLow").toString();
                Long time = Long.parseLong(jsonHolder.get("time").toString());
                displayWeather(summary, tempHigh, tempLow);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayWeather(String summary, String highTemp, String lowTemp) {
        CardView dayCard = new CardView(this);
        dayCard.setMinimumHeight(300);
        TextView cardInfo = new TextView(this);
        cardInfo.setText("\n" + summary + "\n" + "high : " + highTemp + "\n" + "low : " + lowTemp);
        dayCard.addView(cardInfo);
        if(colorFlip = !colorFlip) { // Changes color of card every other time
            dayCard.setCardBackgroundColor(Color.parseColor("#A374FF"));
            cardInfo.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else {
            dayCard.setCardBackgroundColor(Color.parseColor("#ffffff"));
            cardInfo.setTextColor(Color.parseColor("#000000"));
        }
        LinearLayout linearLay = findViewById(R.id.linearLayout);
        linearLay.addView(dayCard);
    }
}
