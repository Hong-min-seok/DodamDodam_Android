package com.example.myapplication.DTO;

public class DeviceDataDTO {
    private String date;
    private String time;
    private float temp;
    private int humi;
    private int mois;

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public float getTemp() {
        return temp;
    }
    public void setTemp(float temp) {
        this.temp = temp;
    }
    public int getHumi() {
        return humi;
    }
    public void setHumi(int humi) {
        this.humi = humi;
    }
    public int getMois() {
        return mois;
    }
    public void setMois(int mois) {
        this.mois = mois;
    }
}
