package com.example.a.dietplanning.db;

import org.litepal.crud.DataSupport;

public class Nutriment extends DataSupport {
    private double heat;  //热量
    private double protein; //蛋白质
    private double fat;     //脂肪
    private double carbohydrate; //碳水化合物

    @Override
    public String toString() {
        return "Nutriment{" +
                "heat=" + heat +
                ", protein=" + protein +
                ", fat=" + fat +
                ", carbohydrate=" + carbohydrate +
                '}';
    }
    public double getHeat() {
        return heat;
    }

    public void setHeat(double heat) {
        this.heat = heat;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }
}
