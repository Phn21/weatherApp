package com.example.weatherappfull;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewModel> {

    private Context context;
    private ArrayList<WeatherItems> weatherItemsArrayList;

    public RecyclerAdapter(Context context, ArrayList<WeatherItems> weatherItemsArrayList) {
        this.context = context;
        this.weatherItemsArrayList = weatherItemsArrayList;
    }

    @NonNull
    @Override
    public ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewModel(LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewModel holder, int position) {

        WeatherItems weatherItems = weatherItemsArrayList.get(position);

        holder.temperature.setText(weatherItems.getTemperature());
        holder.day.setText(weatherItems.getDay());
        holder.weather.setText(weatherItems.getWeatherText());
        holder.feelsLike.setText(weatherItems.getFeelsLike());
        Picasso.get().load("http:".concat(weatherItems.getIcon())).into(holder.icon);

        holder.itemView.setOnClickListener(v -> {


            Intent intent = new Intent(context, itemClicked.class);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("temperature", weatherItems.getTemperature());
            intent.putExtra("weatherText", weatherItems.getWeatherText());
            intent.putExtra("icon", weatherItems.getIcon());
            intent.putExtra("humidity", weatherItems.getAvghumidity());
            intent.putExtra("wind", weatherItems.getMaxwind_kph());
            intent.putExtra("day", weatherItems.getDay());
            intent.putExtra("feelsLike", weatherItems.getFeelsLike());

            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return weatherItemsArrayList.size();
    }


    class ViewModel extends RecyclerView.ViewHolder{

        private TextView temperature, feelsLike, day, weather;
        private ImageView icon;

        public ViewModel(@NonNull View itemView) {
            super(itemView);

            temperature = itemView.findViewById(R.id.textTemperature);
            feelsLike = itemView.findViewById(R.id.textFeelsLike);
            day = itemView.findViewById(R.id.textDayOfWeather);
            weather = itemView.findViewById(R.id.textWeatherOfDay);

            icon = itemView.findViewById(R.id.imageWeatherItem);
        }
    }
}
