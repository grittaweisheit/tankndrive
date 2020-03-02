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

public class FahrtAnzeige extends AppCompatActivity{

    public FahrtDatenQuelle fahrtDb;
    public Button delete, change;
    public FloatingActionButton fab;

    long id;
    String datumLos, zeitLos, ortLos, datumAn, ortAn, zeitAn, zweck;
    int kl, ka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fahrt_anzeige);

        fahrtDb = new FahrtDatenQuelle(this);
        fahrtDb.open();
        Intent intent = getIntent();

        datumLos = intent.getStringExtra("dateDep");
        zeitLos = intent.getStringExtra("timeDep");
        ortLos = intent.getStringExtra("start");
        kl = Integer.parseInt(intent.getStringExtra("kilometersDep"));
        datumAn = intent.getStringExtra("dateArr");
        zeitAn = intent.getStringExtra("timeArr");
        ortAn = intent.getStringExtra("destination");
        ka = Integer.parseInt(intent.getStringExtra("kilometersArr"));
        zweck = intent.getStringExtra("purpose");
        id = intent.getLongExtra("id", 1L);

        ((TextView)(findViewById(R.id.DatumAb))).setText(intent.getStringExtra("dateDep"));
        ((TextView)(findViewById(R.id.ZeitAb))).setText(intent.getStringExtra("timeDep"));
        ((TextView)(findViewById(R.id.OrtAb))).setText(intent.getStringExtra("start"));
        ((TextView)(findViewById(R.id.KilometerAb))).setText(intent.getStringExtra("kilometersDep"));
        ((TextView)(findViewById(R.id.DatumAn))).setText(intent.getStringExtra("dateArr"));
        ((TextView)(findViewById(R.id.ZeitAn))).setText(intent.getStringExtra("timeArr"));
        ((TextView)(findViewById(R.id.OrtAn))).setText(intent.getStringExtra("destination"));
        ((TextView)(findViewById(R.id.KilometerAn))).setText(intent.getStringExtra("kilometersArr"));
        ((TextView)(findViewById(R.id.Zweck))).setText(intent.getStringExtra("purpose"));

        ((TextView)(findViewById(R.id.Kilometer))).setText(String.valueOf(ka - kl));
        ((TextView)(findViewById(R.id.Zeit))).setText("0h 0m 0s");



        fahrtDb.close();
        delete = (Button)findViewById(R.id.DeleteFahrt);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fahrtDb.open();
                fahrtDb.eintragLoeschen(id);

                Intent in = new Intent();
                in.setClassName(getPackageName(), getPackageName()+".FahrtHistory");
                startActivity(in);

                fahrtDb.close();
            }
        });

        change = (Button)findViewById(R.id.ChangeFahrt);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fahrtDb.open();
                Intent intent = new Intent();
                intent.setClassName(getPackageName(), getPackageName()+".FahrtAendern");
                intent.putExtra("dateDep", datumLos);
                intent.putExtra("timeDep", zeitLos);
                intent.putExtra("start", ortLos);
                intent.putExtra("kilometersDep",Integer.toString(kl));
                intent.putExtra("dateArr", datumAn);
                intent.putExtra("timeArr", zeitAn);
                intent.putExtra("destination", ortAn);
                intent.putExtra("kilometersArr", Integer.toString(ka));
                intent.putExtra("purpose", zweck);
                intent.putExtra("id", id);
                startActivity(intent);
                fahrtDb.close();
            }
        });

        fab = (FloatingActionButton)findViewById(R.id.fahrtAnzeigeBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FahrtAnzeige.this, FahrtHistory.class));
            }
        });


    }
}
