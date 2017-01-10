package com.campanale.leonardo.myapplication.utils;

/**
 * Created by root on 26/12/16.
 */

public class Previsione {

    String data;
    String temp;
    String umidita;
    String descr;

    public Previsione(String data, String temp, String umidita, String descr) {
        this.data = data;
        this.temp = temp;
        this.umidita = umidita;
        this.descr = descr;
    }

    @Override
    public String toString() {
        return "" +
                "data='" + data + '\'' +
                ", temp='" + temp + '\'' +
                ", umidita='" + umidita + '\'' +
                ", descr='" + descr + '\'' ;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getUmidita() {
        return umidita;
    }

    public void setUmidita(String umidita) {
        this.umidita = umidita;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
