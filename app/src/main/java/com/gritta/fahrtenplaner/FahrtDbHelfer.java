package com.gritta.fahrtenplaner;

/**
 * Created by Gritta on 13.03.2018.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class FahrtDbHelfer extends SQLiteOpenHelper{

    private HashMap hp;

    public static final String LOG_TAG = FahrtDbHelfer.class.getSimpleName();
    public static final String DB_NAME = "fahrteintraege.db";
    public static final int DB_VERSION = 1;
    public static final String FAHRTEINTRAEGE = "fahrteintraege";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATUM_LOS = "datum_los";
    public static final String COLUMN_DATUM_AN = "datum_an";
    public static final String COLUMN_ZEIT_LOS = "zeit_los";
    public static final String COLUMN_ZEIT_AN = "zeit_an";
    public static final String COLUMN_ORT_LOS = "ort_los";
    public static final String COLUMN_ORT_AN = "ort_an";
    public static final String COLUMN_KILOMETER_LOS = "kilometer_los";
    public static final String COLUMN_KILOMETER_AN = "kilometer_an";
    public static final String COLUMN_ZWECK = "zweck";
    public static final String COLUMN_DATINT = "datum_int";

    public static final String SQL_CREATE =
            "CREATE TABLE " + FAHRTEINTRAEGE +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATUM_LOS + " TEXT NOT NULL, " + COLUMN_DATUM_AN + " TEXT NOT NULL, " +
                    COLUMN_ZEIT_LOS + " TEXT NOT NULL, " + COLUMN_ZEIT_AN + " TEXT NOT NULL, " +
                    COLUMN_KILOMETER_LOS + " INTEGER NOT NULL, " + COLUMN_KILOMETER_AN + " INTEGER NOT NULL, " +
                    COLUMN_ORT_LOS + " TEXT NOT NULL, " + COLUMN_ORT_AN + " TEXT NOT NULL, " +
                    COLUMN_ZWECK + " TEXT NOT NULL, " + COLUMN_DATINT + " INT NOT NULL);";


    public FahrtDbHelfer(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "FahrtDbHelfer hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FAHRTEINTRAEGE);
    }

    public Cursor getFahrt(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + FAHRTEINTRAEGE + " ", null);
        return res;
    }

}
