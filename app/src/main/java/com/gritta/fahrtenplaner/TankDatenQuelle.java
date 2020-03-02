package com.gritta.fahrtenplaner;

/**
 * Created by Gritta on 13.03.2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TankDatenQuelle {

    private static final String LOG_TAG = TankDatenQuelle.class.getSimpleName();

    private SQLiteDatabase tankDb;
    private TankDbHelfer dbHelfer;

    private String[] columns =  {
            TankDbHelfer.COLUMN_ID,
            TankDbHelfer.COLUMN_DATUM,
            TankDbHelfer.COLUMN_KILOMETER,
            TankDbHelfer.COLUMN_ORT,
            TankDbHelfer.COLUMN_STATION,
            TankDbHelfer.COLUMN_LITER,
            TankDbHelfer.COLUMN_BEZAHLT,
            TankDbHelfer.COLUMN_PPL,
            TankDbHelfer.COLUMN_ADDITION,
            TankDbHelfer.COLUMN_DATINT
    };

    public TankDatenQuelle(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelfer.");
        dbHelfer = new TankDbHelfer(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        tankDb = dbHelfer.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + tankDb.getPath());

        //Cursor c = tankDb.query(DbHelfer.TANKEINTRAEGE, IWAS , null, null, null, null, DbHelfer.COLUMN_DATINT + " DESC");
        // tankDb.query(DbHelfer.TANKEINTRAEGE + "ORDER BY " + DbHelfer.COLUMN_DATINT, columns, null, null, null, null, null);

    }

    public void close() {
        dbHelfer.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public Tank erstelleTankeintrag(String d, int ks, String o, String s, double l, double p, double ppl, String a){
        ContentValues werte = new ContentValues();
        werte.put(TankDbHelfer.COLUMN_DATUM, d);
        werte.put(TankDbHelfer.COLUMN_KILOMETER, ks);
        werte.put(TankDbHelfer.COLUMN_ORT, o);
        werte.put(TankDbHelfer.COLUMN_STATION, s);
        werte.put(TankDbHelfer.COLUMN_LITER, l);
        werte.put(TankDbHelfer.COLUMN_BEZAHLT, p);
        werte.put(TankDbHelfer.COLUMN_PPL, ppl);
        werte.put(TankDbHelfer.COLUMN_ADDITION, a);
        werte.put(TankDbHelfer.COLUMN_DATINT, datToInt(d));

        long gibId = tankDb.insert(TankDbHelfer.TANKEINTRAEGE, null, werte);

        Cursor cursor = tankDb.query(TankDbHelfer.TANKEINTRAEGE, columns, TankDbHelfer.COLUMN_ID + "=" + gibId, null, null, null, TankDbHelfer.COLUMN_DATINT + " DESC");
        cursor.moveToFirst();
        Tank t1 = cursorToTank(cursor);
        cursor.close();

        return t1;
    }

    private Tank cursorToTank(Cursor cursor) {

        int idIndex = cursor.getColumnIndex(TankDbHelfer.COLUMN_ID);
        int idDate = cursor.getColumnIndex(TankDbHelfer.COLUMN_DATUM);
        int idKilometers = cursor.getColumnIndex(TankDbHelfer.COLUMN_KILOMETER);
        int idLocation = cursor.getColumnIndex(TankDbHelfer.COLUMN_ORT);
        int idStation = cursor.getColumnIndex(TankDbHelfer.COLUMN_STATION);
        int idTankted = cursor.getColumnIndex(TankDbHelfer.COLUMN_LITER);
        int idPrize = cursor.getColumnIndex(TankDbHelfer.COLUMN_BEZAHLT);
        int idPrizePL = cursor.getColumnIndex(TankDbHelfer.COLUMN_PPL);
        int idAddition = cursor.getColumnIndex(TankDbHelfer.COLUMN_ADDITION);
        int idDatString = cursor.getColumnIndex(TankDbHelfer.COLUMN_DATINT);

        String date = cursor.getString(idDate);
        int kilometers = cursor.getInt(idKilometers);
        long id = cursor.getLong(idIndex);
        String loc = cursor.getString(idLocation);
        String stat = cursor.getString(idStation);
        double lit = cursor.getDouble(idTankted);
        double pr = cursor.getDouble(idPrize);
        double ppl = cursor.getDouble(idPrizePL);
        String add = cursor.getString(idAddition);
        int ds = cursor.getInt(idDatString);

        Tank t = new Tank(id, date, kilometers, loc, stat, lit, pr, ppl, add);

        return t;
    }

    public List<Tank> getAllTanks() {
        List<Tank> tankListe = new ArrayList<>();

        Cursor cursor = tankDb.query(TankDbHelfer.TANKEINTRAEGE, columns, null, null, null, null, TankDbHelfer.COLUMN_DATINT +  " DESC");

        cursor.moveToFirst();
        Tank tank;

        while(!cursor.isAfterLast()) {
            tank = cursorToTank(cursor);
            tankListe.add(tank);
            Log.d(LOG_TAG, "ID: " + tank.getId() + ", Inhalt: " + tank.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return tankListe;
    }

    public void einitragLoeschen(Tank t){
        long id = t.getId();

        tankDb.delete(TankDbHelfer.TANKEINTRAEGE, TankDbHelfer.COLUMN_ID + "=" + id, null);
    }
    public void eintragLoeschen(long od){
        tankDb.delete(TankDbHelfer.TANKEINTRAEGE, TankDbHelfer.COLUMN_ID + "=" + od, null);
    }

    public Tank updateTank(long id, String nd, int nks, String no, String ns, double nl, double np, double nppl, String na) {
        ContentValues values = new ContentValues();
        values.put(TankDbHelfer.COLUMN_DATUM, nd);
        values.put(TankDbHelfer.COLUMN_KILOMETER, nks);
        values.put(TankDbHelfer.COLUMN_ORT, no);
        values.put(TankDbHelfer.COLUMN_STATION, ns);
        values.put(TankDbHelfer.COLUMN_LITER, nl);
        values.put(TankDbHelfer.COLUMN_PPL, nppl);
        values.put(TankDbHelfer.COLUMN_BEZAHLT, np);
        values.put(TankDbHelfer.COLUMN_ADDITION, na);
        values.put(TankDbHelfer.COLUMN_DATINT, datToInt(nd));

        tankDb.update(TankDbHelfer.TANKEINTRAEGE, values, TankDbHelfer.COLUMN_ID + "=" + id, null);

        Cursor cursor = tankDb.query(TankDbHelfer.TANKEINTRAEGE, columns, TankDbHelfer.COLUMN_ID + "=" + id, null, null, null, TankDbHelfer.COLUMN_DATINT + " DESC");

        cursor.moveToFirst();
        Tank t = cursorToTank(cursor);
        cursor.close();

        return t;
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
