package ru.vetatto.investing.investing;

public class PifAutocompleteData {
    private String name, pay, end_date;
    private int id;

    public PifAutocompleteData(String name, int id, String pay, String end_date) {
        this.name = name;
        this.id = id;
        this.pay =pay;
        this.end_date =end_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPay() {return pay;}
    public String getName() {
        return name;
    }
    public String getEndDate() {
        return end_date;
    }

    public void setName(String name) {
        this.name = name;
    }
}
