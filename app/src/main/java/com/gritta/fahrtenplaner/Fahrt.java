package com.gritta.fahrtenplaner;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by Gritta on 12.03.2018.
 */

public class Fahrt {
    private String dat_los, dat_an;
    private String zeit_los, zeit_an;
    private String ort_los, ort_an;
    private int ks_los, ks_an, datInt;
    private String zweck;
    private long id;

    public Fahrt(long i, String dl, String zl, String ol, int kl, String da, String za, String oa, int ka, String z) {
        this.id = i;
        this.dat_an = da;
        this.dat_los = dl;
        this.ks_an = ka;
        this.ks_los = kl;
        this.ort_an = oa;
        this.ort_los = ol;
        this.zeit_an = za;
        this.zeit_los = zl;
        this.zweck = z;
        this.datInt = datToInt(dat_los);

    }

    public int datToInt(String d) {
        String dat1 = d;
        String d1 = "", d2 = "", m1 = "", m2 = "", y1 = "", y2 = "", datumString = "";
        int datumInt = 0;

        int i = 0;
        while (dat1.charAt(i) != '.') {
            d1 += dat1.charAt(i);
            ++i;
        }
        ++i;
        if (d1.length() < 2) d1 = "0" + d1;
        while (dat1.charAt(i) != '.') {
            m1 += dat1.charAt(i);
            ++i;
        }
        ++i;
        if (m1.length() < 2) m1 = "0" + m1;
        while (i < dat1.length()) {
            y1 += dat1.charAt(i);
            ++i;
        }
        ++i;
        if (y1.length() < 4) y1 = "20" + y1;
        datumString = y1 + m1 + d1;
        datumInt = Integer.parseInt(datumString);
        return datumInt;
    }

    @Override
    public String toString(){
        return dat_los + " " + ort_los + " - " + ort_an;
    }

    public void setDat_los(String dat_los) {
        this.dat_los = dat_los;
    }

    public void setDat_an(String dat_an) {
        this.dat_an = dat_an;
    }

    public void setZeit_los(String zeit_los) {
        this.zeit_los = zeit_los;
    }

    public void setZeit_an(String zeit_an) {
        this.zeit_an = zeit_an;
    }

    public int getDatInt() {
        return datInt;
    }

    public void setDatInt(int datInt) {
        this.datInt = datInt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDat_los() {
        return dat_los;
    }

    public String getDat_an() {
        return dat_an;
    }

    public String getZeit_los() {
        return zeit_los;
    }

    public String getZeit_an() {
        return zeit_an;
    }

    public String getOrt_los() {
        return ort_los;
    }

    public void setOrt_los(String ort_los) {
        this.ort_los = ort_los;
    }

    public String getOrt_an() {
        return ort_an;
    }

    public void setOrt_an(String ort_an) {
        this.ort_an = ort_an;
    }

    public int getKs_los() {
        return ks_los;
    }

    public void setKs_los(int ks_los) {
        this.ks_los = ks_los;
    }

    public int getKs_an() {
        return ks_an;
    }

    public void setKs_an(int ks_an) {
        this.ks_an = ks_an;
    }

    public String getZweck() {
        return zweck;
    }

    public void setZweck(String zweck) {
        this.zweck = zweck;
    }
}