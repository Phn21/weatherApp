package com.example.weatherappfull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView currentDateTV, currentTemperatureTV, feelsLikeTV, weatherTextTV;
    private ImageView weatherImage;
    private double latitude, longitude;
    private String cityName = "CityName";

    private ArrayList<WeatherItems> weatherItemsArrayList;
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwipeRefreshLayout swipeRefreshLayout;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).hide();

        currentDateTV = findViewById(R.id.currentDateTV);
        currentTemperatureTV = findViewById(R.id.currentTemperatureTV);
        feelsLikeTV = findViewById(R.id.feelsLikeTV);
        weatherTextTV = findViewById(R.id.weatherTextTV);
        weatherImage = findViewById(R.id.iconWeather);
        recyclerView = findViewById(R.id.recyclerView);

        weatherItemsArrayList = new ArrayList<>();

        statusCheck();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
        while (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED);

        getCityNameWithLoc();
        //SwipeRefresh



        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getCityNameWithLoc();
            }
        });
        swipeRefreshLayout.setColorSchemeColors(Color.CYAN);
    }

    public void getCityNameWithLoc(){

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            cityName = getCityName(longitude, latitude);
                            getWeatherInfo(cityName);
                        }
                    }
                }, Looper.getMainLooper());
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
//            while (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        }
    }

    private void getWeatherInfo(String cityName){
        String url = "http://api.weatherapi.com/v1/forecast.json?key=6a3cfc0837ed4a349c5130000232202&q="+cityName.trim()+"&days=11&aqi=no&alerts=no";

        weatherItemsArrayList.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        //need to write         android:usesCleartextTraffic="true"  in Manifest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String date = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getString("date");

                    Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                    String goal = new SimpleDateFormat("MMM").format(date1);
                    String dateNum = new SimpleDateFormat("d").format(date1);
                    currentDateTV.setText(goal + " " + dateNum);

                    String weatherTxt = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    weatherTextTV.setText(weatherTxt);
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(weatherImage);

                    String temperature = response.getJSONObject("current").getString("temp_c");
                    currentTemperatureTV.setText(temperature+"°");

                    String temperatureF = response.getJSONObject("current").getString("feelslike_c");
                    feelsLikeTV.setText(temperatureF+"°");



                    JSONObject forecastOBJ = response.getJSONObject("forecast");
                    JSONArray forcastO = forecastOBJ.getJSONArray("forecastday");

                    int a = forcastO.length();
                    for (int i = 1; i<forcastO.length(); i++){
                        JSONObject forecastDay = forcastO.getJSONObject(i);
                        String dateItem = forecastDay.getString("date");
                        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(dateItem);
                        String weekDay2 = new java.text.SimpleDateFormat("EEEE").format(date2);
                        String goal2 = new SimpleDateFormat("MMM").format(date2);
                        String dateNum2 = new SimpleDateFormat("d").format(date2);

                        String maxTemperature = forecastDay.getJSONObject("day").getString("maxtemp_c");
                        String minTemperature = forecastDay.getJSONObject("day").getString("mintemp_c");
                        String weatherText = forecastDay.getJSONObject("day").getJSONObject("condition").getString("text");
                        String icon = forecastDay.getJSONObject("day").getJSONObject("condition").getString("icon");
                        String maxwind_kph = forecastDay.getJSONObject("day").getString("maxwind_kph");
                        String avghumidity = forecastDay.getJSONObject("day").getString("avghumidity");

                        String day = weekDay2 + ", " + goal2 + " " + dateNum2;

                        weatherItemsArrayList.add(new WeatherItems(minTemperature, maxTemperature, icon, day, weatherText, maxwind_kph, avghumidity));

                        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), weatherItemsArrayList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(recyclerAdapter);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to enable Mobile Location?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private String getCityName(double longitude, double latitude){
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);

            for (Address adr : addresses){
                if (adr!=null){
                    String city = adr.getLocality();
                    if (city!=null && !city.equals("")){
                        cityName = city;
//                        currentDateTV.setText(cityName);
                        Log.d("CityName", cityName);
                        break;
                    }else {
                        Log.d("TAG", "CITY NOT FOUND");
                        Toast.makeText(this, "User City Not Found..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }
}