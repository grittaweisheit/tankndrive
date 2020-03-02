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


public class TankHistory extends AppCompatActivity {

    private TankDatenQuelle tankDB;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tankDB = new TankDatenQuelle(this);

        final String ordnerPfad = Environment.getExternalStorageDirectory().getPath() + "/Backup/";

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.plusThing);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( TankHistory.this, TankEintrag.class));
            }
        });
        FloatingActionButton fab1 = (FloatingActionButton)findViewById(R.id.historyBack);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TankHistory.this, Start.class));
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
                TankDbHelfer tankDbHelfer = new TankDbHelfer(getApplicationContext());
                tankDB.open();
                final Cursor cu = tankDbHelfer.getTank();
                List<Tank> tanks = tankDB.getAllTanks();
                tankDB.close();
                File sd = Environment.getExternalStorageDirectory();
                String csvFile = "tankeintraege.xls";
                File ordner = new File(sd.getAbsolutePath());

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    String[] s = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(TankHistory.this, s, 0);
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
                    WritableSheet sheet = workbook.createSheet("tankList", 0);

                    sheet.addCell(new Label(0,0,"Datum"));
                    sheet.addCell(new Label(1,0,"Ort"));
                    sheet.addCell(new Label(2,0,"Tankstelle"));
                    sheet.addCell(new Label(3,0,"Kilometerstand"));
                    sheet.addCell(new Label(4,0,"getankte Liter"));
                    sheet.addCell(new Label(5,0,"Bezahlt"));
                    sheet.addCell(new Label(6,0,"Preis pro Liter"));
                    sheet.addCell(new Label(7,0,"Ergänzungen"));

                    for (int i = 0; i < tanks.size(); ++i){
                        Tank t = tanks.get(i);
                        String d = t.getDat();
                        String o = t.getOrt();
                        String s = t.getStation();
                        String ks = String.valueOf(t.getKilometerstand());
                        String l = String.valueOf(t.getLiter());
                        String b = String.valueOf(t.getPrize());
                        String ppl = String.valueOf(t.getPrizePl());
                        String ad = t.getAddition();

                        sheet.addCell(new Label(0,i+1, d));
                        sheet.addCell(new Label(1,i+1, o));
                        sheet.addCell(new Label(2,i+1, s));
                        sheet.addCell(new Label(3,i+1, ks));
                        sheet.addCell(new Label(4,i+1, l));
                        sheet.addCell(new Label(5,i+1, b));
                        sheet.addCell(new Label(6,i+1, ppl));
                        sheet.addCell(new Label(7,i+1, ad));
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

        tankDB.open();
        List<Tank> tanks = tankDB.getAllTanks();
        tankDB.close();

        ListAdapter adapter = new ArrayAdapter<Tank>(getApplicationContext(), R.layout.historyeintrag_element, tanks);

        final ListView lv = (ListView)findViewById(R.id.list_view);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {

                Intent intent = new Intent();
                intent.setClassName(getPackageName(), getPackageName()+".TankAnzeige");
                Tank t = (Tank)lv.getAdapter().getItem(arg2);
                intent.putExtra("date", t.getDat());
                intent.putExtra("location", t.getOrt());
                intent.putExtra("kilometers", Integer.toString(t.getKilometerstand()));
                intent.putExtra("liters", Double.toString(t.getLiter()));
                intent.putExtra("prize", Double.toString(t.getPrize()));
                intent.putExtra("prizePL", Double.toString(t.getPrizePl()));
                intent.putExtra("station", (t.getStation()));
                intent.putExtra("addition", t.getAddition());
                intent.putExtra("id", t.getId());
                startActivity(intent);
            }
        });
    }

    private void initializeContextualActionBar(){

        final ListView tanksListView = (ListView) findViewById(R.id.list_view);
        tanksListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        tanksListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

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
                        SparseBooleanArray touchedShoppingMemosPositions = tanksListView.getCheckedItemPositions();
                        tankDB.open();
                        for (int i=0; i < touchedShoppingMemosPositions.size(); i++) {
                            boolean isChecked = touchedShoppingMemosPositions.valueAt(i);
                            if(isChecked) {
                                int postitionInListView = touchedShoppingMemosPositions.keyAt(i);
                                Tank tank = (Tank) tanksListView.getItemAtPosition(postitionInListView);
                                Log.d(TankEintrag.LOG_TAG, "Position im ListView: " + postitionInListView + " Inhalt: " + tank.toString());
                                tankDB.einitragLoeschen(tank);
                            }
                        }
                        showAllListEntries();
                        tankDB.close();
                        mode.finish();
                        return true;
                    case R.id.cab_change:
                        SparseBooleanArray touchedTankPositions1 = tanksListView.getCheckedItemPositions();

                        for (int i=0; i < touchedTankPositions1.size(); i++) {
                            boolean isChecked = touchedTankPositions1.valueAt(i);
                            if(isChecked) {
                                tankDB.open();
                                int postitionInListView = touchedTankPositions1.keyAt(i);
                                Tank tank = (Tank) tanksListView.getItemAtPosition(postitionInListView);
                                Log.d(TankEintrag.LOG_TAG, "Position im ListView: " + postitionInListView + " Inhalt: " + tank.toString());

                                Intent intent = new Intent();
                                intent.setClassName(getPackageName(), getPackageName()+".TankAendern");
                                intent.putExtra("date", tank.getDat());
                                intent.putExtra("location", tank.getOrt());
                                intent.putExtra("kilometers", Integer.toString(tank.getKilometerstand()));
                                intent.putExtra("liters", Double.toString(tank.getLiter()));
                                intent.putExtra("prize", Double.toString(tank.getPrize()));
                                intent.putExtra("prizePL", Double.toString(tank.getPrizePl()));
                                intent.putExtra("station", tank.getStation());
                                intent.putExtra("addition", tank.getAddition());
                                intent.putExtra("id", String.valueOf(tank.getId()));
                                startActivity(intent);
                                tankDB.close();
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
        List<Tank> tankL = tankDB.getAllTanks();

        ArrayAdapter<Tank> tankArrayAdapter = new ArrayAdapter<> (
                this,
                R.layout.historyeintrag_element,
                tankL);

        ListView tankListView = (ListView) findViewById(R.id.list_view);
        tankListView.setAdapter(tankArrayAdapter);
    }

}