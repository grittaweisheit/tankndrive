package com.gritta.fahrtenplaner;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Gritta on 12.03.2018.
 */

public class Tank{
    private String dat;
    private int kilometerstand;
    private String ort, station;
    private double liter;
    private double prizePl, prize;
    private String addition;
    private long id;
    private int datInt;

    public Tank(long id, String d, int ks, String o, String s,double l, double p, double ppl, String a){
        this.dat = d;
        this.station = s;
        this.kilometerstand = ks;
        this.ort = o;
        this.liter = l;
        this.prize = p;
        this.prizePl = ppl;
        this.addition = a;
        this.id = id;
        datInt = this.datToInt(d);

    }

    @Override
    public String toString(){
        return this.dat + " " + this.station + " " + this.ort + " " + this.kilometerstand + " " + this.addition;
    }

    public boolean wasBefore(Tank t){
        String dat1 = dat;
        String dat2 = t.getDat();

        String d1 = "", d2 = "", m1 = "", m2 = "", y1 = "", y2 = "";
        int days1 = 0, days2 = 0, months1 = 0, months2 = 0, years1 = 0, years2 = 0;

        int i = 0;
        while (dat1.charAt(i) != '.'){
            d1 += dat1.charAt(i);
            ++i;
        } days1 = Integer.parseInt(d1);
        while (dat2.charAt(i) != '.'){
            d2 += dat2.charAt(i);
            ++i;
        } days2 = Integer.parseInt(d2);
        while (dat1.charAt(i) != '.'){
            m1 += dat1.charAt(i);
            ++i;
        } months1 = Integer.parseInt(m1);
        while (dat2.charAt(i) != '.'){
            m2 += dat2.charAt(i);
            ++i;
        } months2 = Integer.parseInt(m2);
        while (dat1.charAt(i) != '.'){
            y1 += dat1.charAt(i);
            ++i;
        } years1 = Integer.parseInt(y1);
        while (dat2.charAt(i) != '.'){
            y2 += dat2.charAt(i);
            ++i;
        } years2 = Integer.parseInt(y2);

        if (years1 < years2) return true;
        if (months1 < months2) return true;
        if (days1 < days2) return true;

        return false;
    }

    public int datToInt(String d){
        String dat1 = d;
        String d1 = "", d2 = "", m1 = "", m2 = "", y1 = "", y2 = "", datumString = "";
        int datumInt = 0;

        int i = 0;
        while (dat1.charAt(i) != '.'){
            d1 += dat1.charAt(i);
            ++i;
        }++i; if(d1.length() < 2) d1 ="0" + d1;
        while (dat1.charAt(i) != '.'){
            m1 += dat1.charAt(i);
            ++i;
        } ++i; if(m1.length() < 2) m1 ="0" + m1;
        while (i < dat1.length()){
            y1 += dat1.charAt(i);
            ++i;
        } ++i; if (y1.length() < 4) y1 = "20" + y1;
        datumString = y1 + m1 + d1;
        datumInt = Integer.parseInt(datumString);
        return datumInt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDatInt(){ return datInt;}

    public void setDatInt(int di){ this.datInt = di;}

    public String getStation() {
        return station;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {this.addition = addition; }

    public void setStation(String station) {
        this.station = station;
    }

    public double getPrizePl() {
        return prizePl;
    }

    public void setPrizePl(double prizePl) {
        this.prizePl = prizePl;
    }

    public double getPrize() {
        return prize;
    }

    public void setPrize(double prize) {
        this.prize = prize;
    }

    public String getDat() {
        return dat;
    }

    public void setDat(String dat) {
        this.dat = dat;
    }

    public int getKilometerstand() {
        return kilometerstand;
    }

    public void setKilometerstand(int kilometerstand) {
        this.kilometerstand = kilometerstand;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public double getLiter() {
        return liter;
    }

    public void setLiter(double liter) {
        this.liter = liter;
    }
}
