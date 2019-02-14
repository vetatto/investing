package ru.vetatto.investing.investing.Billing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BillingData {
    String title;
    String amount;
    String income;
    String procent;
    String apiToken;

    public BillingData(String apiToken, String title, String amount, String income,String procent) {
        this.title = title;
        this.procent =procent;
        this.apiToken = apiToken;
        this.amount = amount;
        this.income=income;
    }

    public String getApiToken(){return apiToken;}
    public String getBillTitle(){
        return title;
    }
    public String getBillAmount(){
        return amount;
    }
    public String getBillIncome(){
        return income;
    }
    public String getBillProcent() {return procent;}
}