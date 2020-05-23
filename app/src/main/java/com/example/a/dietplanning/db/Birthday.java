package com.example.a.dietplanning.db;

import java.io.Serializable;

public class Birthday implements Serializable {
    private int year;
    private int month;

    @Override
    public String toString() {
        return  year +"年" +
                 + month +"月"
                 + day +"日";
    }

    private int day;
    public Birthday(){
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
}
