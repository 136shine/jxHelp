package com.jxthelp.bean;

/**
 * Created by idisfkj on 15/10/29.
 * Email : idisfkj@qq.com.
 */
public class WeatherInfo {
    private String location;
    private String date;
    private String riQi;
    private String shiShi;
    private String dayPicture;
    private String nightPicture;
    private String weather;
    private String wind;
    private String dayTemperature;
    private String nightTemperature;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRiQi() {
        return riQi;
    }

    public void setRiQi(String riQi) {
        this.riQi = riQi;
    }

    public String getShiShi() {
        return shiShi;
    }

    public void setShiShi(String shiShi) {
        this.shiShi = shiShi;
    }

    public String getDayPicture() {
        return dayPicture;
    }

    public void setDayPicture(String dayPicture) {
        this.dayPicture = dayPicture;
    }

    public String getNightPicture() {
        return nightPicture;
    }

    public void setNightPicture(String nightPicture) {
        this.nightPicture = nightPicture;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getDayTemperature() {
        return dayTemperature;
    }

    public void setDayTemperature(String dayTemperature) {
        this.dayTemperature = dayTemperature;
    }

    public String getNightTemperature() {
        return nightTemperature;
    }

    public void setNightTemperature(String nightTemperature) {
        this.nightTemperature = nightTemperature;
    }

    @Override
    public String toString() {
        return "WeatherInfo{" +
                "location='" + location + '\'' +
                ", date='" + date + '\'' +
                ", riQi='" + riQi + '\'' +
                ", shiShi='" + shiShi + '\'' +
                ", dayPicture='" + dayPicture + '\'' +
                ", nightPicture='" + nightPicture + '\'' +
                ", weather='" + weather + '\'' +
                ", wind='" + wind + '\'' +
                ", dayTemperature='" + dayTemperature + '\'' +
                ", nightTemperature='" + nightTemperature + '\'' +
                '}';
    }
}
