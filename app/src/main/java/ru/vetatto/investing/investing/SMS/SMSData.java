package ru.vetatto.investing.investing.SMS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SMSData {
    String title;
    String from;
    String info_operation;


    public SMSData(String from, String title, String info_operation) {
        this.title = title;
        this.from=from;
        this.info_operation=info_operation;
    }

    public String getSMSFrom(){return from;}
    public String getSMSTitle(){
        return title;
    }
    public String getSMSinfoOperation(){
        return info_operation;
    }

}