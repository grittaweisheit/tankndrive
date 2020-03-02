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

public class TankDbHelfer extends SQLiteOpenHelper{

    private HashMap hp;

    public static final String LOG_TAG = TankDbHelfer.class.getSimpleName();
    public static final String DB_NAME = "tankeintraege.db";
    public static final int DB_VERSION = 1;
    public static final String TANKEINTRAEGE = "tankeintraege";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATUM = "datum";
    public static final String COLUMN_KILOMETER = "kilometer";
    public static final String COLUMN_ORT = "ort";
    public static final String COLUMN_STATION = "station";
    public static final String COLUMN_LITER = "tanked";
    public static final String COLUMN_BEZAHLT = "prize";
    public static final String COLUMN_PPL = "prizePL";
    public static final String COLUMN_ADDITION = "addition";
    public static final String COLUMN_DATINT = "datum_int";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TANKEINTRAEGE +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATUM + " TEXT NOT NULL, " +
                    COLUMN_KILOMETER + " INTEGER NOT NULL, " + COLUMN_ORT + " TEXT NOT NULL, "
                    + COLUMN_STATION + " TEXT NOT NULL, " + COLUMN_LITER + " INTEGER, "
                    + COLUMN_BEZAHLT + " DOUBLE, " + COLUMN_PPL + " DOUBLE, "
                    + COLUMN_ADDITION + " TEXT, " + COLUMN_DATINT + " INT NOT NULL);";


    public TankDbHelfer(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelfer hat die Datenbank: " + getDatabaseName() + " erzeugt.");
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
        db.execSQL("DROP TABLE IF EXISTS " + TANKEINTRAEGE);
    }

    public Cursor getTank(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TANKEINTRAEGE + " ", null);
        return res;
    }

}
