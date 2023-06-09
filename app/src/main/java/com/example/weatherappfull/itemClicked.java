package com.example.weatherappfull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class itemClicked extends AppCompatActivity implements View.OnClickListener {

    private TextView currentDateTV, currentTemperatureTV, feelsLikeTV, weatherTextTV, maxwind_kphTV, avghumidity;
    private ImageView iconWeather, shareBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_clicked);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).hide();

        iconWeather = findViewById(R.id.iconWeatherItem);
        currentDateTV = findViewById(R.id.currentDateTVItem);
        currentTemperatureTV = findViewById(R.id.currentTemperatureTVItem);
        feelsLikeTV = findViewById(R.id.feelsLikeTVItem);
        weatherTextTV = findViewById(R.id.weatherTextTVItem);
        maxwind_kphTV = findViewById(R.id.maxwind_kphTVItem);
        avghumidity = findViewById(R.id.avghumidityItem);
        shareBT = findViewById(R.id.shareIC);


        currentTemperatureTV.setText(getIntent().getStringExtra("temperature"));
        currentTemperatureTV.append("°");
        currentDateTV.setText(getIntent().getStringExtra("day"));
        feelsLikeTV.append(getIntent().getStringExtra("feelsLike"));
        weatherTextTV.setText(getIntent().getStringExtra("weatherText"));
        maxwind_kphTV.append(getIntent().getStringExtra("wind"));
        avghumidity.append(getIntent().getStringExtra("humidity"));
        Picasso.get().load("http:".concat(getIntent().getStringExtra("icon"))).into(iconWeather);

        shareBT.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.shareIC){
            Toast.makeText(this, "SMS Sent", Toast.LENGTH_SHORT).show();

            Intent email = new Intent(Intent.ACTION_SEND);
//            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"soselia.aleksandre@gmail.com"});
            email.putExtra(Intent.EXTRA_SUBJECT, "The temperature of " + currentDateTV.getText());
            email.putExtra(Intent.EXTRA_TEXT, "Temp: " + currentTemperatureTV.getText() + "\n " + feelsLikeTV.getText() + "\n" + maxwind_kphTV.getText() + "\n" + avghumidity.getText());
            email.setType("message/rfc822");

            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        }
    }
}