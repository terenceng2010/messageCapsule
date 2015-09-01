package com.terence;

public class WeatherConditionItem {

    public final String text;
    public final int icon;
    public WeatherConditionItem(String text, Integer icon) {
        this.text = text;
        this.icon = icon;
    }
    @Override
    public String toString() {
        return text;
    }
}
