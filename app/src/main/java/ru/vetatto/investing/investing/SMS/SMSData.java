package ru.vetatto.investing.investing.SMS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SMSData {
    String title;
    String from;


    public SMSData(String from, String title) {
        this.title = title;
        this.from=from;
    }

    public String getSMSFrom(){return from;}
    public String getSMSTitle(){
        return title;
    }

}