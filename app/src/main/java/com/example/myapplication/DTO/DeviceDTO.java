package com.example.myapplication.DTO;

import java.util.ArrayList;

public class DeviceDTO {
    private String device_id;
    private flowerDTO flower;
    private String[] water;
    private ArrayList<DeviceDataDTO> data;

    public ArrayList<DeviceDataDTO> getData() {
        return data;
    }
    public void setData(ArrayList<DeviceDataDTO> data) {
        this.data = data;
    }
    public String[] getWater() {
        return water;
    }
    public void setWater(String[] water) {
        this.water = water;
    }
    public String getDevice_id() {
        return device_id;
    }
    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
    public flowerDTO getFlower() {
        return flower;
    }
    public void setFlower(flowerDTO flower) {
        this.flower = flower;
    }
}
