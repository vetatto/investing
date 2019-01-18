package ru.vetatto.investing.investing.PifInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PifOperationData {
    String title;
    float sr_price;
    float type_operation;
    float date_price;
    String sum_price;
    String ukTitle;
    int pifId;
    String date;
    float procent;
    String apiToken;
    String nameCat;
    int type_instrument;
    float sum_money;

    public PifOperationData(String apiToken, String title, float date_price, float type_operation, String sum_price, int pifId, float sr_price, String ukTitle, String date, float procent, int type_instrument, String nameCat, float sum_money) {
        this.title = title;
        this.sr_price = sr_price;
        this.date_price = date_price;
        this.type_operation = type_operation;
        this.sum_price = sum_price;
        this.pifId = pifId;
        this.ukTitle = ukTitle;
        this.date = date;
        this.procent =procent;
        this.apiToken = apiToken;
        this.type_instrument = type_instrument;
        this.nameCat=nameCat;
        this.sum_money=sum_money;
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
    public float getPifOperationType(){ return type_operation; }
    public float getPifDatePrice(){ return date_price; }
    public int getTypeInstrument(){ return type_instrument; }
    public int getPifId(){ return pifId; }
    public float getProcent() {return procent;}
    public float getSum_money() {return sum_money;}
    public String getukTitle(){ return ukTitle; }
}
