package com.gritta.fahrtenplaner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Gritta on 14.03.2018.
 */

public class TankAendern extends AppCompatActivity{
    Button ok;
    String dat, ort, station, add;
    int ks;
    long id;
    double lit, pr, ppl;
    Intent in;

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

        in = getIntent();

        ((EditText)findViewById(R.id.DatumE)).setText(in.getStringExtra("date"));
        ((EditText)(findViewById(R.id.OrtE))).setText(in.getStringExtra("location"));
        ((EditText)(findViewById(R.id.ZeitAb))).setText(in.getStringExtra("kilometers"));
        ((EditText)(findViewById(R.id.getankteLiterE))).setText(in.getStringExtra("liters"));

        int statPos = adapter.getPosition(in.getStringExtra("station"));
        spinner.setSelection(statPos);

        ((EditText)(findViewById(R.id.BezahltE))).setText(in.getStringExtra("prize"));
        ((EditText)(findViewById(R.id.Preis_pro_LiterE))).setText(in.getStringExtra("prizePL"));
        ((EditText)(findViewById(R.id.ErgaenzungenE))).setText(in.getStringExtra("addition"));
        id = Long.parseLong(in.getStringExtra("id"));

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
                } else pr = Double.parseDouble(p.getText().toString());

                EditText pl = (EditText)findViewById(R.id.Preis_pro_LiterE);
                if(pl.getText().toString().equals("")){
                    ppl = 0;
                }else ppl = Double.parseDouble(pl.getText().toString());

                EditText a = (EditText)findViewById(R.id.ErgaenzungenE);
                if(a.getText().toString().equals("")){
                    add = " ";
                }else add = a.getText().toString();

                tankDb = new TankDatenQuelle(TankAendern.this);

                Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
                tankDb.open();

                tankDb.updateTank(Long.parseLong(in.getStringExtra("id")), dat, ks, ort, station, lit, pr, ppl, add);

                Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
                tankDb.close();

                Log.d(LOG_TAG, "Datum ist " + dat);

                startActivity(new Intent(TankAendern.this, TankHistory.class));
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
