package com.gritta.fahrtenplaner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.content.Intent;

import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.widget.AbsListView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by Gritta on 12.03.2018.
 */


public class FahrtHistory extends AppCompatActivity {

    private FahrtDatenQuelle fahrtDb;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fahrtDb = new FahrtDatenQuelle(this);

        final String ordnerPfad = Environment.getExternalStorageDirectory().getPath() + "/Backup/";

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.plusThing);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( FahrtHistory.this, FahrtEintrag.class));
            }
        });
        FloatingActionButton fab1 = (FloatingActionButton)findViewById(R.id.historyBack);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FahrtHistory.this, Start.class));
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton)findViewById(R.id.thingToExcel);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                /*
                File tankTab = Environment.getExternalStorageDirectory();
                File file = new File(ordnerPfad);
                File ordner = new File(tankTab.getAbsolutePath());
                if (!file.exists()) {
                    file.mkdirs();
                }
                tankDB.open();
                //List<Tank> tanks = tankDB.getAllTanks();
                SQLiteToExcel sqLiteToExcel = new SQLiteToExcel(getApplicationContext(), TankDbHelfer.DB_NAME, ordnerPfad);
                sqLiteToExcel.exportSingleTable(TankDbHelfer.TANKEINTRAEGE, TankDbHelfer.TANKEINTRAEGE + ".xsl", new SQLiteToExcel.ExportListener() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onCompleted(String filePath) {
                        Toast.makeText(getApplication(), "Läuft", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getApplication(), "Fehler", Toast.LENGTH_SHORT).show();
                    }
                });
                tankDB.close(); */
                FahrtDbHelfer fahrtDbHelfer = new FahrtDbHelfer(getApplicationContext());
                fahrtDb.open();
                final Cursor cu = fahrtDbHelfer.getFahrt();
                List<Fahrt> fahrten = fahrtDb.getAllFahrten();
                fahrtDb.close();
                File sd = Environment.getExternalStorageDirectory();
                String csvFile = "fahrteintraege.xls";
                File ordner = new File(sd.getAbsolutePath());

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    String[] s = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(FahrtHistory.this, s, 0);
                    return;
                } else {}

                if (!ordner.isDirectory()){
                    ordner.mkdirs();
                }try {
                    File file = new File(ordner, csvFile);
                    WorkbookSettings wbSettings = new WorkbookSettings();
                    wbSettings.setLocale(new Locale("de", "DE"));
                    WritableWorkbook workbook;
                    workbook = Workbook.createWorkbook(file, wbSettings);
                    WritableSheet sheet = workbook.createSheet("fahrtList", 0);

                    sheet.addCell(new Label(0,0,"Datum"));
                    sheet.addCell(new Label(1,0,"Von"));
                    sheet.addCell(new Label(2,0,"Nach"));
                    sheet.addCell(new Label(3,0,"Zweck"));
                    sheet.addCell(new Label(4,0,"Ankunft"));
                    sheet.addCell(new Label(5,0,"Kilometer"));
                    sheet.addCell(new Label(6,0,"Dauer"));

                    for (int i = 0; i < fahrten.size(); ++i){
                        Fahrt t = fahrten.get(i);
                        String dat = t.getDat_los();
                        String sta = t.getOrt_los();
                        String zie = t.getOrt_an();
                        String zwe = t.getZweck();
                        String ank = t.getDat_an() + " " + t.getZeit_an();
                        String kil = String.valueOf(t.getKs_an() - t.getKs_los());
                        String dau = "X h  Y m  Z  s";

                        sheet.addCell(new Label(0,i+1, dat));
                        sheet.addCell(new Label(1,i+1, sta));
                        sheet.addCell(new Label(2,i+1, zie));
                        sheet.addCell(new Label(3,i+1, zwe));
                        sheet.addCell(new Label(4,i+1, ank));
                        sheet.addCell(new Label(5,i+1, kil));
                        sheet.addCell(new Label(6,i+1, dau));
                    }
                    workbook.write();
                    workbook.close();
                    Toast.makeText(getApplication(), "Dateien in Excel Sheet exportiert", Toast.LENGTH_SHORT).show();

                } catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplication(), "Fehler!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        initializeContextualActionBar();

        fahrtDb.open();
        List<Fahrt> fahrten = fahrtDb.getAllFahrten();
        fahrtDb.close();

        ListAdapter adapter = new ArrayAdapter<Fahrt>(getApplicationContext(), R.layout.historyeintrag_element, fahrten);

        final ListView lv = (ListView)findViewById(R.id.list_view);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {

                Intent intent = new Intent();
                intent.setClassName(getPackageName(), getPackageName()+".FahrtAnzeige");
                Fahrt t = (Fahrt) lv.getAdapter().getItem(arg2);
                intent.putExtra("dateDep", t.getDat_los());
                intent.putExtra("timeDep", (t.getZeit_los()));
                intent.putExtra("start", t.getOrt_los());
                intent.putExtra("kilometersDep", Integer.toString(t.getKs_los()));
                intent.putExtra("dateArr", t.getDat_an());
                intent.putExtra("timeArr", (t.getZeit_an()));
                intent.putExtra("destination", t.getOrt_an());
                intent.putExtra("kilometersArr", Integer.toString(t.getKs_an()));
                intent.putExtra("purpose", t.getZweck());
                intent.putExtra("id", t.getId());
                startActivity(intent);
            }
        });
    }

    private void initializeContextualActionBar(){

        final ListView fahrtListView = (ListView) findViewById(R.id.list_view);
        fahrtListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        fahrtListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_contextual_action_bar, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.cab_delete:
                        SparseBooleanArray touchedShoppingMemosPositions = fahrtListView.getCheckedItemPositions();
                        fahrtDb.open();
                        for (int i=0; i < touchedShoppingMemosPositions.size(); i++) {
                            boolean isChecked = touchedShoppingMemosPositions.valueAt(i);
                            if(isChecked) {
                                int postitionInListView = touchedShoppingMemosPositions.keyAt(i);
                                Fahrt fahrt = (Fahrt) fahrtListView.getItemAtPosition(postitionInListView);
                                Log.d(TankEintrag.LOG_TAG, "Position im ListView: " + postitionInListView + " Inhalt: " + fahrt.toString());
                                fahrtDb.einitragLoeschen(fahrt);
                            }
                        }
                        showAllListEntries();
                        fahrtDb.close();
                        mode.finish();
                        return true;
                    case R.id.cab_change:
                        SparseBooleanArray touchedFahrtPositions1 = fahrtListView.getCheckedItemPositions();

                        for (int i=0; i < touchedFahrtPositions1.size(); i++) {
                            boolean isChecked = touchedFahrtPositions1.valueAt(i);
                            if(isChecked) {
                                fahrtDb.open();
                                int postitionInListView = touchedFahrtPositions1.keyAt(i);
                                Fahrt t = (Fahrt) fahrtListView.getItemAtPosition(postitionInListView);
                                //Log.d(TankEintrag.LOG_TAG, "Position im ListView: " + postitionInListView + " Inhalt: " + fahrt.toString());

                                Intent intent = new Intent();
                                intent.setClassName(getPackageName(), getPackageName()+".FahrtAnzeige");
                                intent.putExtra("dateDep", t.getDat_los());
                                intent.putExtra("timeDep", (t.getZeit_los()));
                                intent.putExtra("start", t.getOrt_los());
                                intent.putExtra("kilometersDep", Integer.toString(t.getKs_los()));
                                intent.putExtra("dateArr", t.getDat_an());
                                intent.putExtra("timeArr", (t.getZeit_an()));
                                intent.putExtra("destination", t.getOrt_an());
                                intent.putExtra("kilometersArr", Integer.toString(t.getKs_an()));
                                intent.putExtra("purpose", t.getZweck());
                                intent.putExtra("id", t.getId());
                                startActivity(intent);
                                fahrtDb.close();
                            }
                        }
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
    }
    private void showAllListEntries () {
        List<Fahrt> fahrtL = fahrtDb.getAllFahrten();

        ArrayAdapter<Fahrt> fahrtArrayAdapter = new ArrayAdapter<> (
                this,
                R.layout.historyeintrag_element,
                fahrtL);

        ListView fahrtListView = (ListView) findViewById(R.id.list_view);
        fahrtListView.setAdapter(fahrtArrayAdapter);
    }

}