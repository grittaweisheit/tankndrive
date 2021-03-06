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


public class FahrtEintrag extends AppCompatActivity{

    Button ok;

    long id;
    String datumLos, zeitLos, ortLos, datumAn, ortAn, zeitAn, zweck, heute, jetzt;
    int kl, ka;

    public static final String LOG_TAG = FahrtEintrag.class.getSimpleName();

    private FahrtDatenQuelle fahrtDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fahrt_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        VERSION version = new Build.VERSION();
        //Datum auf heute setzen
        if (version.SDK_INT < 26){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            heute = dateFormat.format(calendar.getTime());
            ((EditText) (findViewById(R.id.DatumAbE))).setText(heute);
            ((EditText) (findViewById(R.id.DatumAnE))).setText(heute);
        }
        else {
            LocalDate h = LocalDate.now();
            heute = String.valueOf(h.getDayOfMonth()) + "." + String.valueOf(h.getMonthValue()) + "." + String.valueOf(h.getYear());
            ((EditText) (findViewById(R.id.DatumAbE))).setText(heute);
            ((EditText) (findViewById(R.id.DatumAnE))).setText(heute);
        }
        ((EditText)findViewById(R.id.DatumAbE)).setText(heute);

        //OK Button
        ok = (Button) findViewById(R.id.FahrteintragErstellen);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText d = (EditText)findViewById(R.id.DatumAbE);
                if(d.getText().toString().equals("")){
                    fehlenderEintragToast();
                    return;
                }
                datumLos = d.getText().toString();

                EditText k = (EditText)findViewById(R.id.OrtAnE);
                if(k.getText().toString().equals("")){
                    fehlenderEintragToast();
                    return;
                }
                ortAn = (k.getText()).toString();

                EditText o = (EditText)findViewById(R.id.OrtAbE);
                if(o.getText().toString().equals("")){
                    fehlenderEintragToast();
                    return;
                }
                ortLos = o.getText().toString();

                //optionale Eingaben
                EditText l = (EditText)findViewById(R.id.DatumAnE);
                if(l.getText().toString().equals("")){
                    datumAn = datumLos;
                }else datumAn = l.getText().toString();

                EditText p = (EditText)findViewById(R.id.ZeitAbE);
                if(p.getText().toString().equals("")){
                    zeitLos = "00:00";
                }else zeitLos = p.getText().toString();

                EditText za = (EditText)findViewById(R.id.ZeitAnE);
                if(p.getText().toString().equals("")){
                    zeitAn = "00:00";
                }else zeitAn = za.getText().toString();

                EditText pl = (EditText)findViewById(R.id.KilometerAbE);
                if(pl.getText().toString().equals("")){
                    kl = 0;
                }else ka = Integer.parseInt(pl.getText().toString());

                EditText a = (EditText)findViewById(R.id.KilometerAnE);
                if(a.getText().toString().equals("")){
                    ka = 0;
                }else ka = Integer.parseInt(a.getText().toString());

                EditText zw = (EditText)findViewById(R.id.ZweckE);
                if(a.getText().toString().equals("")){
                    zweck = "Nur so";
                }else zweck = a.getText().toString();

                fahrtDb = new FahrtDatenQuelle(FahrtEintrag.this);

                Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
                fahrtDb.open();

                fahrtDb.erstelleFahrteintrag(datumLos, zeitLos, ortLos, kl, datumAn, zeitAn, ortAn, ka, zweck);

                Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
                fahrtDb.close();

                startActivity(new Intent(FahrtEintrag.this, FahrtHistory.class));
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
