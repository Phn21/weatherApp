package com.example.weatherappfull;

public class WeatherItems {
    private String feelsLike;
    private String temperature;
    private String icon;
    private String day;
    private String weatherText, maxwind_kph, avghumidity;

    public WeatherItems(String feelsLike, String temperature, String icon, String day, String weatherText, String maxwind_kph, String avghumidity) {
        this.feelsLike = feelsLike;
        this.temperature = temperature;
        this.icon = icon;
        this.day = day;
        this.weatherText = weatherText;
        this.maxwind_kph = maxwind_kph;
        this.avghumidity = avghumidity;
    }

    public String getMaxwind_kph() {
        return maxwind_kph;
    }

    public String getAvghumidity() {
        return avghumidity;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getIcon() {
        return icon;
    }

    public String getDay() {
        return day;
    }

    public String getWeatherText() {
        return weatherText;
    }
}
