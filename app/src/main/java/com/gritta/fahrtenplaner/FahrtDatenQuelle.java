package com.gritta.fahrtenplaner;

/**
 * Created by Gritta on 13.03.2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class FahrtDatenQuelle {

    private static final String LOG_TAG = FahrtDatenQuelle.class.getSimpleName();

    private SQLiteDatabase fahrtDb;
    private FahrtDbHelfer dbHelfer;

    private String[] columns =  {
            FahrtDbHelfer.COLUMN_ID,
            FahrtDbHelfer.COLUMN_DATUM_AN,
            FahrtDbHelfer.COLUMN_KILOMETER_AN,
            FahrtDbHelfer.COLUMN_ORT_LOS,
            FahrtDbHelfer.COLUMN_ZEIT_AN,
            FahrtDbHelfer.COLUMN_ZEIT_LOS,
            FahrtDbHelfer.COLUMN_DATUM_LOS,
            FahrtDbHelfer.COLUMN_KILOMETER_LOS,
            FahrtDbHelfer.COLUMN_ORT_AN,
            FahrtDbHelfer.COLUMN_ZWECK,
            FahrtDbHelfer.COLUMN_DATINT
    };

    public FahrtDatenQuelle(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelfer.");
        dbHelfer = new FahrtDbHelfer(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        fahrtDb = dbHelfer.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + fahrtDb.getPath());

        //Cursor c = tankDb.query(DbHelfer.TANKEINTRAEGE, IWAS , null, null, null, null, DbHelfer.COLUMN_DATINT + " DESC");
        // tankDb.query(DbHelfer.TANKEINTRAEGE + "ORDER BY " + DbHelfer.COLUMN_DATINT, columns, null, null, null, null, null);

    }

    public void close() {
        dbHelfer.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public Fahrt erstelleFahrteintrag(String dl, String zl, String ol, int kl, String da, String za, String oa, int ka, String z) {
        ContentValues values = new ContentValues();
        values.put(FahrtDbHelfer.COLUMN_DATUM_LOS, dl);
        values.put(FahrtDbHelfer.COLUMN_ZEIT_LOS, zl);
        values.put(FahrtDbHelfer.COLUMN_ORT_LOS, ol);
        values.put(FahrtDbHelfer.COLUMN_KILOMETER_LOS, kl);
        values.put(FahrtDbHelfer.COLUMN_DATUM_AN, da);
        values.put(FahrtDbHelfer.COLUMN_ZEIT_AN, za);
        values.put(FahrtDbHelfer.COLUMN_ORT_AN, oa);
        values.put(FahrtDbHelfer.COLUMN_KILOMETER_AN, ka);
        values.put(FahrtDbHelfer.COLUMN_ZWECK, z);
        values.put(FahrtDbHelfer.COLUMN_DATINT, datToInt(dl));

        long gibId = fahrtDb.insert(FahrtDbHelfer.FAHRTEINTRAEGE, null, values);

        Cursor cursor = fahrtDb.query(FahrtDbHelfer.FAHRTEINTRAEGE, columns, FahrtDbHelfer.COLUMN_ID + "=" + gibId, null, null, null, FahrtDbHelfer.COLUMN_DATINT + " DESC");
        cursor.moveToFirst();
        Fahrt t1 = cursorToFahrt(cursor);
        cursor.close();

        return t1;
    }

    private Fahrt cursorToFahrt(Cursor cursor) {

        int idIndex = cursor.getColumnIndex(FahrtDbHelfer.COLUMN_ID);
        int idDateDep = cursor.getColumnIndex(FahrtDbHelfer.COLUMN_DATUM_LOS);
        int idTimeDep = cursor.getColumnIndex(FahrtDbHelfer.COLUMN_ZEIT_LOS);
        int idKilometersDep = cursor.getColumnIndex(FahrtDbHelfer.COLUMN_KILOMETER_LOS);
        int idStart = cursor.getColumnIndex(FahrtDbHelfer.COLUMN_ORT_LOS);
        int idDateArr = cursor.getColumnIndex(FahrtDbHelfer.COLUMN_DATUM_AN);
        int idTimeArr = cursor.getColumnIndex(FahrtDbHelfer.COLUMN_ZEIT_AN);
        int idKilometersArr = cursor.getColumnIndex(FahrtDbHelfer.COLUMN_KILOMETER_AN);
        int idDestination = cursor.getColumnIndex(FahrtDbHelfer.COLUMN_ORT_AN);
        int idPurpose = cursor.getColumnIndex(FahrtDbHelfer.COLUMN_ZWECK);
        int idDatInt = cursor.getColumnIndex(FahrtDbHelfer.COLUMN_DATINT);

        long id = cursor.getLong(idIndex);
        String dateDep = cursor.getString(idDateDep);
        String timeDep = cursor.getString(idTimeDep);
        int kilometersDep = cursor.getInt(idKilometersDep);
        String start = cursor.getString(idStart);
        String dateArr = cursor.getString(idDateArr);
        String timeArr = cursor.getString(idTimeArr);
        int kilometersArr = cursor.getInt(idKilometersArr);
        String destination = cursor.getString(idDestination);
        String purpose = cursor.getString(idPurpose);
        int ds = cursor.getInt(idDatInt);

        Fahrt f = new Fahrt(id, dateDep, timeDep, start, kilometersDep, dateArr, timeArr, destination, kilometersArr, purpose);

        return f;
    }

    public List<Fahrt> getAllFahrten() {
        List<Fahrt> fahrtListe = new ArrayList<>();

        Cursor cursor = fahrtDb.query(FahrtDbHelfer.FAHRTEINTRAEGE, columns, null, null, null, null, FahrtDbHelfer.COLUMN_DATINT +  " DESC");

        cursor.moveToFirst();
        Fahrt fahrt;

        while(!cursor.isAfterLast()) {
            fahrt = cursorToFahrt(cursor);
            fahrtListe.add(fahrt);
            Log.d(LOG_TAG, "ID: " + fahrt.getId() + ", Inhalt: " + fahrt.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return fahrtListe;
    }

    public void einitragLoeschen(Fahrt t){
        long id = t.getId();

        fahrtDb.delete(FahrtDbHelfer.FAHRTEINTRAEGE, FahrtDbHelfer.COLUMN_ID + "=" + id, null);
    }
    public void eintragLoeschen(long od){
        fahrtDb.delete(FahrtDbHelfer.FAHRTEINTRAEGE, FahrtDbHelfer.COLUMN_ID + "=" + od, null);
    }

    public Fahrt updateFahrt(long i, String dl, String zl, String ol, int kl, String da, String za, String oa, int ka, String z) {
        ContentValues values = new ContentValues();
        values.put(FahrtDbHelfer.COLUMN_ID, i);
        values.put(FahrtDbHelfer.COLUMN_DATUM_LOS, dl);
        values.put(FahrtDbHelfer.COLUMN_ZEIT_LOS, zl);
        values.put(FahrtDbHelfer.COLUMN_ORT_LOS, ol);
        values.put(FahrtDbHelfer.COLUMN_KILOMETER_LOS, kl);
        values.put(FahrtDbHelfer.COLUMN_DATUM_AN, da);
        values.put(FahrtDbHelfer.COLUMN_ZEIT_AN, za);
        values.put(FahrtDbHelfer.COLUMN_ORT_AN, oa);
        values.put(FahrtDbHelfer.COLUMN_KILOMETER_AN, ka);
        values.put(FahrtDbHelfer.COLUMN_ZWECK, z);
        values.put(FahrtDbHelfer.COLUMN_DATINT, datToInt(dl));

        fahrtDb.update(FahrtDbHelfer.FAHRTEINTRAEGE, values, FahrtDbHelfer.COLUMN_ID + "=" + i, null);

        Cursor cursor = fahrtDb.query(FahrtDbHelfer.FAHRTEINTRAEGE, columns, FahrtDbHelfer.COLUMN_ID + "=" + i, null, null, null, FahrtDbHelfer.COLUMN_DATINT + " DESC");

        cursor.moveToFirst();
        Fahrt f = cursorToFahrt(cursor);
        cursor.close();

        return f;
    }
    public int datToInt(String d){
        String dat1 = d;
        String d1 = "", d2 = "", m1 = "", m2 = "", y1 = "", y2 = "", datumString = "";
        int datumInt = 0;

        int i = 0;
        while (dat1.charAt(i) != '.'){
            d1 += dat1.charAt(i);
            ++i;
        } ++i; if(d1.length() < 2) d1 ="0" + d1;
        while (dat1.charAt(i) != '.'){
            m1 += dat1.charAt(i);
            ++i;
        } ++i; if(m1.length() < 2) m1 ="0" + m1;
        while (i < dat1.length()){
            y1 += dat1.charAt(i);
            ++i;
        }if (y1.length() < 4) y1 = "20" + y1;
        datumString = y1 + m1 + d1;
        datumInt = Integer.parseInt(datumString);
        return datumInt;
    }
}
