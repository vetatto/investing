package ru.vetatto.investing.investing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PifData{
    String title;
    float sr_price;
    float sum_amount;
    float date_price;
    String sum_price;
    String ukTitle;
    int pifId;
    String date;
    float procent;
    String apiToken;
    String nameCat;
    int type_instrument;

    PifData(String apiToken, String title, float date_price, float sum_amount, String sum_price, int pifId, float sr_price, String ukTitle, String date, float procent, int type_instrument, String nameCat) {
        this.title = title;
        this.sr_price = sr_price;
        this.date_price = date_price;
        this.sum_amount = sum_amount;
        this.sum_price = sum_price;
        this.pifId = pifId;
        this.ukTitle = ukTitle;
        this.date = date;
        this.procent =procent;
        this.apiToken = apiToken;
        this.type_instrument = type_instrument;
        this.nameCat=nameCat;
    }

    public String getApiToken(){return apiToken;}
    public String getPifTitle(){
        return title;
    }
    public String getPifNameCat(){
        return nameCat;
    }
    public float getPifSrPrice(){
        return sr_price;
    }
    public float getPifSumAmount(){ return sum_amount; }
    public float getPifDatePrice(){ return date_price; }
    public int getTypeInstrument(){ return type_instrument; }
    public int getPifId(){ return pifId; }
    public float getProcent() {return procent;}
    public String getDate(){
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        Date dates = null;
        try {
            dates = oldDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = newDateFormat.format(dates);
        return result;}
    public String getukTitle(){ return ukTitle; }
}