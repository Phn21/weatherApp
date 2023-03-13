package com.example.weatherappfull;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class itemClicked extends AppCompatActivity {

    private TextView currentDateTV, currentTemperatureTV, feelsLikeTV, weatherTextTV, maxwind_kphTV, avghumidity;
    private ImageView iconWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_clicked);

        iconWeather = findViewById(R.id.iconWeatherItem);
        currentDateTV = findViewById(R.id.currentDateTVItem);
        currentTemperatureTV = findViewById(R.id.currentTemperatureTVItem);
        feelsLikeTV = findViewById(R.id.feelsLikeTVItem);
        weatherTextTV = findViewById(R.id.weatherTextTVItem);
        maxwind_kphTV = findViewById(R.id.maxwind_kphTVItem);
        avghumidity = findViewById(R.id.avghumidityItem);


        currentTemperatureTV.setText(getIntent().getStringExtra("temperature"));
        currentTemperatureTV.append("°");
        currentDateTV.setText(getIntent().getStringExtra("day"));
        feelsLikeTV.append(getIntent().getStringExtra("feelsLike"));
        weatherTextTV.setText(getIntent().getStringExtra("weatherText"));
        maxwind_kphTV.append(getIntent().getStringExtra("wind"));
        avghumidity.append(getIntent().getStringExtra("humidity"));
        Picasso.get().load("http:".concat(getIntent().getStringExtra("icon"))).into(iconWeather);

    }
}