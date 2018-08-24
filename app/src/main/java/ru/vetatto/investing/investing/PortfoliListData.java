package ru.vetatto.investing.investing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PortfoliListData {
    String title;
    int id;
    String apiToken;
    String goal_name;
    float goal_amount;


    PortfoliListData(String apiToken, int id, String title, String goal_name, float goal_amount) {
        this.title = title;
        this.goal_name = goal_name;
        this.apiToken = apiToken;
        this.goal_amount = goal_amount;
        this.id = id;

    }
    public String getGoalName(){return  goal_name;}
    public float getGoalAmount() {return goal_amount;}
    public String getApiToken(){return apiToken;}
    public String getPifTitle(){
        return title;
    }
    public int getId(){ return id; }
}