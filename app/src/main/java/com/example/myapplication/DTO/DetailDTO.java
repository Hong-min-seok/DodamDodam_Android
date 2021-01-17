package com.example.myapplication.DTO;

public class DetailDTO {
    private String device_id;
    private String f_nick;
    private String f_name;
    private String f_lang;
    private String f_use;

    public String getF_nick() {
        return f_nick;
    }

    public void setF_nick(String f_nick) {
        this.f_nick = f_nick;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getF_lang() {
        return f_lang;
    }

    public void setF_lang(String f_lang) {
        this.f_lang = f_lang;
    }

    public String getF_use() {
        return f_use;
    }

    public void setF_use(String f_use) {
        this.f_use = f_use;
    }

    public String getF_shape() {
        return f_shape;
    }

    public void setF_shape(String f_shape) {
        this.f_shape = f_shape;
    }

    private String f_shape;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
