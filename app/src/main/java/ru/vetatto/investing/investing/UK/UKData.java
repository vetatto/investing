package ru.vetatto.investing.investing.UK;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UKData {
    String title;
    URL image;

    public UKData(String title, URL image) {
        this.title = title;
        this.image=image;
    }

    public String getUKTitle(){return title;}
    public URL getUKImage(){
        return image;
    }
}