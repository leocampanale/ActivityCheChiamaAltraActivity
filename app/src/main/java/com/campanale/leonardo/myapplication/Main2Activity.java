package com.campanale.leonardo.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.campanale.leonardo.myapplication.utils.Previsione;
import com.campanale.leonardo.myapplication.utils.RichiestaHttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    ListView myListView;  // 13.7251097,100.3529083
    String [] coords = {"lat=41.1079&lon=16.8637", "lat=41.9102415&lon=12.3959122", "lat=50.848446&lon=4.3512057",
                        "lat=13.7251097&lon=100.3529083", "lat=40.111&lon=17.055"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle dati = getIntent().getExtras();
        int selectedCity = dati.getInt("citta", 4);
        String coord=coords[selectedCity];

        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myListView = (ListView)findViewById(R.id.mylistview);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Esci e torna", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();

            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // creazione di un “builder di alert dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Main2Activity.this); // builder di AlterDialog
                // impostazione dei testi dei pulsanti positivo e negativo
                alertDialogBuilder.setTitle("Conferma uscita");
                alertDialogBuilder.setMessage("Vuoi terminare l'app?");
                // ai pulsanti possono essere collegati dei listener
                alertDialogBuilder.setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                alertDialogBuilder.setNegativeButton("No",
                        null);
                alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
            // creazione e visualizzazione messaggio:
                alertDialogBuilder.create().show();
            }
        });

        String url= "http://api.openweathermap.org/data/2.5/forecast?"+
                coord +  "&units=metric&appid="+
                "e4dc66b316a46b96910367017313d1d7";
        RichiestaHttpGet richiesta = null;
        try {
            richiesta = new RichiestaHttpGet(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        richiesta.doRequest(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                TextView txt=(TextView)findViewById(R.id.textView2);
                boolean b = msg.getData().getBoolean("success", false);
                if (b) {
                    String jsonres=msg.getData().getString("result","-");
                    try {
                        JSONObject jo = new JSONObject(jsonres);
                        String citta=jo.getJSONObject("city").getString("name");
                        txt.setText("Previsioni per "+citta);
                    } catch (JSONException e) {
                        Log.i("PREV",e.getLocalizedMessage() );
                    }
                    List<Previsione> previsioni = leggiDati(jsonres);
                    ArrayAdapter<Previsione> arrayAdapter =
                            new ArrayAdapter<Previsione>(Main2Activity.this, R.layout.row, R.id.textViewList,
                                    previsioni);
                    myListView.setAdapter(arrayAdapter);

                    // msg.getData().getString("result","-"));
                } else {
                    txt.setText(msg.getData().getString("error","??"));
                }
            }
        });



    }

    private List<Previsione> leggiDati(String jsonStr) {

        List<Previsione> result = new ArrayList<>();
        if (jsonStr.startsWith("-"))
            return result;
        try {
            JSONObject jo = new JSONObject(jsonStr);
            JSONArray lista = jo.getJSONArray("list");
            for (int i = 0; i < lista.length(); i++) {
                JSONObject elemento = lista.getJSONObject(i);
                String data = elemento.getString("dt_txt");
                String temp = elemento.getJSONObject("main").getString("temp");
                String umidita = elemento.getJSONObject("main").getString("humidity");
                String descr = elemento.getJSONArray("weather").getJSONObject(0).getString("description");

                Previsione p = new Previsione(data, temp, umidita, descr);
                result.add(p);

            }
        } catch (JSONException e) {
            Log.i("PREV",e.getLocalizedMessage() );
        }

        return result;
    }

}
