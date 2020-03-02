package com.gritta.fahrtenplaner;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Gritta on 12.03.2018.
 */

public class TankAnzeige extends AppCompatActivity{

    public TankDatenQuelle tankdb;
    public Button delete, change;
    public FloatingActionButton fab;

    long id;
    String dat, ort, stat, add;
    int ks;
    double lit, p, ppl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tank_anzeige);

        tankdb = new TankDatenQuelle(this);
        tankdb.open();
        Intent intent = getIntent();

        dat = intent.getStringExtra("date");
        ks = Integer.parseInt(intent.getStringExtra("kilometers"));
        ort = intent.getStringExtra("location");
        stat = intent.getStringExtra("station");
        lit = Double.parseDouble(intent.getStringExtra("liters"));
        p = Double.parseDouble(intent.getStringExtra("prize"));
        ppl = Double.parseDouble(intent.getStringExtra("prizePL"));
        add = intent.getStringExtra("addition");
        id = intent.getLongExtra("id", 1L);

        ((TextView)(findViewById(R.id.DatumAb))).setText(intent.getStringExtra("date"));
        ((TextView)(findViewById(R.id.OrtAb))).setText(intent.getStringExtra("location"));
        ((TextView)(findViewById(R.id.ZeitAb))).setText(intent.getStringExtra("kilometers"));
        ((TextView)(findViewById(R.id.Zweck))).setText(intent.getStringExtra("liters"));
        ((TextView)(findViewById(R.id.KilometerAb))).setText(intent.getStringExtra("station"));
        ((TextView)(findViewById(R.id.Bezahlt))).setText(intent.getStringExtra("prize"));
        ((TextView)(findViewById(R.id.Preis_pro_Liter))).setText(intent.getStringExtra("prizePL"));
        ((TextView)(findViewById(R.id.Ergaenzung))).setText(intent.getStringExtra("addition"));


        tankdb.close();
        delete = (Button)findViewById(R.id.DeleteFahrt);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tankdb.open();
                tankdb.eintragLoeschen(id);

                Intent in = new Intent();
                in.setClassName(getPackageName(), getPackageName()+".TankHistory");
                startActivity(in);

                tankdb.close();
            }
        });

        change = (Button)findViewById(R.id.Change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tankdb.open();
                Intent intent = new Intent();
                intent.setClassName(getPackageName(), getPackageName()+".TankAendern");
                intent.putExtra("date", dat);
                intent.putExtra("location", ort);
                intent.putExtra("kilometers", Integer.toString(ks));
                intent.putExtra("liters", Double.toString(lit));
                intent.putExtra("prize", Double.toString(p));
                intent.putExtra("prizePL", Double.toString(ppl));
                intent.putExtra("station", stat);
                intent.putExtra("addition", add);
                intent.putExtra("id", Long.toString(id));
                startActivity(intent);
                tankdb.close();

            }
        });

        fab = (FloatingActionButton)findViewById(R.id.tankAnzeigeBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TankHistory.class));
            }
        });


    }
}
