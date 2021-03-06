package com.campanale.leonardo.myapplication.utils;

/**
 * Created by root on 20/12/16.
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by root on 01/12/16.
 */

public class RichiestaHTTP implements Serializable {

    private String url;
    private String params;
    private boolean isFree;
    private final String USER_AGENT = "Mozilla/5.0";
    //private Thread thread;

    public RichiestaHTTP() {

    }

    public RichiestaHTTP(String url) throws MalformedURLException {
        this.url = url;
        //thread = null;
        isFree = true;
    }

    public RichiestaHTTP(String url, JSONObject params) throws MalformedURLException, JSONException {
        this.url = url;
        setParams(params);
        //thread = null;
        isFree = true;
    }

    public void setParams(JSONObject params) throws JSONException {
        StringBuilder par = new StringBuilder();
        this.params = new String();

        //Costruisco una stringa dei parametri a partire dal JSON
        Iterator<String> keys = params.keys();
        int i = 0;
        while (keys.hasNext()) {
            if (i > 0)
                par.append("&");
            String key = keys.next();
            par.append(key).append("=").append(params.getString(key));
            i++;
        }
        par.append("\r\n");

        this.params = par.toString();
    }

    public boolean isFree() {
        return isFree;
    }

    public interface ResponseListener {
        void onStart(String params);
        void onResponse(Exception exception, String response);
        void onFinish();
    }

    public void doRequest(final ResponseListener responseListener) {
        new Thread() {
            @Override
            public void run() {

                responseListener.onStart(params.toString());
                isFree = false;

                Exception exception = null;
                StringBuilder response = new StringBuilder();
                HttpURLConnection connection = null;
                BufferedReader read = null;

                try {
                    connection = (HttpURLConnection) new URL(url).openConnection();

                    //Imposto header e richiesta HTTP
                    connection.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    connection.setRequestProperty("charset", "UTF-8");
                    connection.setRequestProperty("Content-Length", Integer
                            .toString(params.toString().length()));
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");

                    //Parametri richiesta
                    String data = params;
                    Log.d("LOG", data);

                    //Scrivo i paramatri
                    OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                    wr.write(data);
                    wr.flush();
                    wr.close();

                    //Ottengo la risposta dal server
                    read = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = read.readLine();
                    while (line != null) {
                        response.append(line);
                        line = read.readLine();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    exception = e;
                }

                responseListener.onResponse(exception, response.toString());

                try {
                    Thread.sleep(500);
                    read.close();
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                isFree = true;

                responseListener.onFinish();
            }
        }.start();
    }


    public void doRequest(final Handler handler) {
        new Thread() {
            @Override
            public void run() {


                isFree = false;

                Exception exception = null;
                StringBuilder response = new StringBuilder();
                HttpURLConnection connection = null;
                BufferedReader read = null;

                try {
                    connection = (HttpURLConnection) new URL(url).openConnection();

                    //Imposto header e richiesta HTTP
                    connection.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    connection.setRequestProperty("charset", "UTF-8");

                    connection.setRequestMethod("GET");


                    connection.setRequestProperty("User-Agent", USER_AGENT);

                    int responseCode = connection.getResponseCode();



                    //Ottengo la risposta dal server
                    read = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = read.readLine();
                    while (line != null) {
                        response.append(line);
                        line = read.readLine();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    exception = e;
                }

                Message m = new Message();
                Bundle b = new Bundle();
                b.putBoolean("success", exception==null);
                b.putString("result", response.toString());
                if (exception!=null) b.putString("error", exception.getLocalizedMessage());
                m.setData(b);
                handler.sendMessage(m);

                try {
                    Thread.sleep(500);
                    read.close();
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                isFree = true;

              //   responseListener.onFinish();
            }
        }.start();
    }
}