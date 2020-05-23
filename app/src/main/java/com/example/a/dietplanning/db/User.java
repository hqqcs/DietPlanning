package com.example.a.dietplanning.db;

import java.io.Serializable;

public class User implements Serializable {
    public static User user=new User();
    private String userName;//用户名
    private String sex;//用户性别
    private double height;//用户的身高
    private double weight;//用户的体重
    private Birthday userBirthday;//用户的出生年月
    private double coef;   //运动强度
    private int target;//目标

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }


    public double getCoef() {
        return coef;
    }

    public void setCoef(double coef) {
        this.coef = coef;
    }

    public User(){
    }


    public Birthday getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(Birthday userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
