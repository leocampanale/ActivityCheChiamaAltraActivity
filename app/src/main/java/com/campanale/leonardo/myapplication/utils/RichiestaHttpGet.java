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

public class RichiestaHttpGet implements Serializable {

    private String url;


    private final String USER_AGENT = "Mozilla/5.0";
    //private Thread thread;


    public RichiestaHttpGet(String url) throws MalformedURLException {
        this.url = url;

    }


    public void doRequest(final Handler handler) {
        new Thread() {
            @Override
            public void run() {


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
                  //  Thread.sleep(500);
                    read.close();
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }
}