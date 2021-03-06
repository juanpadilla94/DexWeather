package com.example.dexweather;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private String apiKey;
    public double latitude;
    public double longitude;
    private String url;
    private boolean colorFlip;

    // API KEY: 0b3a33b127b30991aad586510ac20441
    // https://api.darksky.net/forecast/0b3a33b127b30991aad586510ac20441/37.8267,-122.4233
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiKey = "0b3a33b127b30991aad586510ac20441";
        latitude = 32.7157;
        longitude = -117.1611;
        url = "https://api.darksky.net/forecast/"
                + apiKey + "/"
                + latitude + ","
                + longitude;
        getWeather();
        Button refreshButton = (Button)findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLay = findViewById(R.id.linearLayout);
                linearLay.removeAllViews();
                getWeather();
                Toast.makeText(MainActivity.this,
                        "Page Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getWeather() {
        try {
            // GET Request for data
            String jsonGetResult = new WeatherModel().execute(url).get();
            String daily = new JSONObject(jsonGetResult).get("daily").toString();
            String data = new JSONObject(daily).get("data").toString();
            JSONArray jsonArr = new JSONArray(data);
            colorFlip = true;
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            long timeMillis = System.currentTimeMillis();
            // iterate through all days for weather
            for(int i = 0; i < jsonArr.length(); i++) {
                Date date = new Date(timeMillis);
                String weatherDay = dateFormat.format(date);
                JSONObject jsonHolder = new JSONObject(jsonArr.get(i).toString());
                String summary = jsonHolder.get("summary").toString();
                String tempHigh = jsonHolder.get("apparentTemperatureHigh").toString();
                String tempLow = jsonHolder.get("apparentTemperatureLow").toString();
                String humidity = jsonHolder.get("humidity").toString();
                String pressure = jsonHolder.get("pressure").toString();
                String windSpeed = jsonHolder.get("windSpeed").toString();
                // weather on UI
                displayWeather(weatherDay, summary, tempHigh, tempLow, humidity, pressure, windSpeed);
                timeMillis += TimeUnit.DAYS.toMillis(1);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void displayWeather(String time, String summary, String highTemp, String lowTemp,
                                final String humidity, final String pressure, final String windSpeed) {
        CardView weatherCard = new CardView(this);
        final CardView dayCard = new CardView(this);
        weatherCard.setMinimumHeight(300);
        TextView cardInfo = new TextView(this);
        cardInfo.setText("\n" + time + "\n" + "\n" + summary + "\n" + "high : " + highTemp + "\n" + "low : " + lowTemp);
        weatherCard.addView(cardInfo);
        if(colorFlip = !colorFlip) { // Changes color of card every other time
            weatherCard.setCardBackgroundColor(Color.parseColor("#A374FF"));
            cardInfo.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else {
            weatherCard.setCardBackgroundColor(Color.parseColor("#ffffff"));
            cardInfo.setTextColor(Color.parseColor("#000000"));
        }
        // display view of particular day selected and show extra info
        weatherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayCard.setMinimumHeight(800);
                dayCard.setCardBackgroundColor(Color.parseColor("#A374FF"));
                TextView dayInfo = new TextView(MainActivity.this);
                dayInfo.setTextColor(Color.parseColor("#FFFFFF"));
                dayInfo.setText("\n" + "humidity: " + humidity
                        + "\n" + "pressure : " + pressure
                        + "\n" + "windspeed : " + windSpeed);
                dayCard.addView(dayInfo);
                LinearLayout linearLay = findViewById(R.id.linearLayout);
                linearLay.removeAllViews();
                linearLay.addView(dayCard);
                Button backButton = (Button)findViewById(R.id.back_button);
                backButton.setVisibility(View.VISIBLE);
            }
        });
        // Return to results i.e. take out view from stack
        Button backButton = (Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLay = findViewById(R.id.linearLayout);
                linearLay.removeAllViews();
                getWeather();
            }
        });
        backButton.setVisibility(View.INVISIBLE);
        LinearLayout linearLay = findViewById(R.id.linearLayout);
        linearLay.addView(weatherCard);
    }
}
