package ru.vetatto.investing.investing.GraphicEditActiv;

public class GraphicLegendData {
    String title;
    int color;

    public GraphicLegendData(String title, int color) {
        this.title = title;
        this.color = color;
    }

    public String getGraphicLegendTitle(){ return title; }
    public int getGraphicLegendColor(){ return color; }
}
