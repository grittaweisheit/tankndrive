package com.gritta.fahrtenplaner;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

import android.util.Log;
import android.widget.Toast;

import android.os.Build.VERSION;


/**
 * Created by Gritta on 12.03.2018.
 */


public class TankEintrag extends AppCompatActivity{

    Button ok;
    String dat, ort, station, add, heute;
    int ks;
    double lit, pr, ppl;

    public static final String LOG_TAG = TankEintrag.class.getSimpleName();

    private TankDatenQuelle tankDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tank_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinner = (Spinner) findViewById(R.id.TankstelleE);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.stations, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        VERSION version = new Build.VERSION();
        //Datum auf heute setzen
        if (version.SDK_INT < 26){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            heute = dateFormat.format(calendar.getTime());
        }
        else {
            LocalDate h = LocalDate.now();
            heute = String.valueOf(h.getDayOfMonth()) + "." + String.valueOf(h.getMonthValue()) + "." + String.valueOf(h.getYear());
            ((EditText) (findViewById(R.id.DatumE))).setText(heute);
        }
        ((EditText)findViewById(R.id.DatumE)).setText(heute);

        //OK Button
        ok = (Button) findViewById(R.id.TankEintragErstellen);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText d = (EditText)findViewById(R.id.DatumE);
                if(d.getText().toString().equals("")){
                    fehlenderEintragToast();
                    return;
                }
                dat = d.getText().toString();

                EditText k = (EditText)findViewById(R.id.ZeitAb);
                if(k.getText().toString().equals("")){
                    fehlenderEintragToast();
                    return;
                }
                ks = Integer.parseInt((k.getText()).toString());

                EditText o = (EditText)findViewById(R.id.OrtE);
                if(o.getText().toString().equals("")){
                    fehlenderEintragToast();
                    return;
                }
                ort = o.getText().toString();

                Spinner st = (Spinner)findViewById(R.id.TankstelleE);
                st.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
                        Object item = parent.getItemAtPosition(pos);
                    }
                    public void onNothingSelected(AdapterView<?> parent){
                    }
                });
                station = st.getSelectedItem().toString();

                EditText l = (EditText)findViewById(R.id.getankteLiterE);
                if(l.getText().toString().equals("")){
                    lit = 0;
                }else lit = Double.parseDouble(l.getText().toString());

                EditText p = (EditText)findViewById(R.id.BezahltE);
                if(p.getText().toString().equals("")){
                    pr = 0;
                }else pr = Double.parseDouble(p.getText().toString());

                EditText pl = (EditText)findViewById(R.id.Preis_pro_LiterE);
                if(pl.getText().toString().equals("")){
                    ppl = 0;
                }else ppl = Double.parseDouble(pl.getText().toString());

                EditText a = (EditText)findViewById(R.id.ErgaenzungenE);
                if(a.getText().toString().equals("")){
                    add = " ";
                }else add = a.getText().toString();

                tankDb = new TankDatenQuelle(TankEintrag.this);

                Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
                tankDb.open();

                tankDb.erstelleTankeintrag(dat, ks, ort, station, lit, pr, ppl, add);

                Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
                tankDb.close();

                Log.d(LOG_TAG, "Datum ist " + dat);

                startActivity(new Intent(TankEintrag.this, TankHistory.class));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void fehlenderEintragToast(){
        Context context = getApplicationContext();
        CharSequence text = "Eingabe nicht vorllständig!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
