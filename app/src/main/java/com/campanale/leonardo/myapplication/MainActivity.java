package com.campanale.leonardo.myapplication;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {



    Fragment fragment=null;
    int selectedCity=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();
        CharSequence text = "Applicazione attivata";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);

        toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
        toast.show();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // recupero spinner
        Spinner sp = (Spinner)findViewById(R.id.spinner);
        // aggiungo i dati
        String[] lista ={"Bari", "Roma","Bruxelles", "Bangkok"};
       // creando un ArrayAdapter
        ArrayAdapter spinAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, lista);
        // che collego allo spinner
        sp.setAdapter(spinAdapter);
        sp.setPrompt("Selezionare una città");
       sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if (fragment!=null)
                   getSupportFragmentManager().beginTransaction().remove(fragment).commit();
               fragment=null;
               selectedCity=i;
               switch (i) {
                   case 0:
                     fragment = new BariFragment();
                       getSupportFragmentManager().beginTransaction()
                               .add(R.id.framelayout, fragment).commit();
                       break;
                   case 1:
                       fragment = new RomaFragment();
                       getSupportFragmentManager().beginTransaction()
                               .add(R.id.framelayout, fragment).commit();

               }

           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Vedi previsioni", Snackbar.LENGTH_LONG)
                        .setAction("Click qui!",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                                Bundle b = new Bundle();
                                b.putInt("citta", selectedCity);
                                i.putExtras(b);
                                startActivity(i);

                            }
                        }).
                        show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
