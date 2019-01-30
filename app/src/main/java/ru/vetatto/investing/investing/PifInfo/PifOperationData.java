package ru.vetatto.investing.investing.PifInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PifOperationData {
    float amount_pay;
    float sr_price;
    float type_operation;
    String date_price;
    String date_operation;

    public PifOperationData(float amount_pay, String date_operation, String date_price, float type_operation) {
        this.amount_pay = amount_pay;
        this.date_operation=date_operation;
        this.date_price = date_price;
        this.type_operation = type_operation;

    }

    public float getPifOperationType(){ return type_operation; }
    public float getPifOperationAmountPay(){ return amount_pay; }
    public String getPifOperationDate(){return date_operation;}
    public String getPifOperationPrice(){return date_price;}
}
