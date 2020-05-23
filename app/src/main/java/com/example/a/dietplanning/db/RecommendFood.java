package com.example.a.dietplanning.db;

import org.litepal.crud.DataSupport;

public class RecommendFood extends DataSupport {
    private int id;
    private String food_name;//食物的名称
    private Double foodG;//食物克数
    private Double energy;//食物的热量
    private Double rl;//100g食物的热量
//    private String url;//图片地址
    private Double dbz;
    private Double cshhw;
    private Double zf;
    private String type;
    private String data;
    private Boolean isComplete;//打卡是否完成
    private String kind;//推荐的食物三餐类型

    public RecommendFood(){}
    public RecommendFood(String food_name, Double foodG, Double energy, Double rl, Double dbz, Double cshhw, Double zf, String type, boolean isComplete, String kind) {
        this.food_name = food_name;
        this.foodG = foodG;
        this.energy=energy;
        this.rl = rl;
        this.dbz = dbz;
        this.cshhw = cshhw;
        this.zf = zf;
        this.type = type;
        this.isComplete=isComplete;
        this.kind=kind;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getDbz() {
        return dbz;
    }

    public void setDbz(Double dbz) {
        this.dbz = dbz;
    }

    public Double getCshhw() {
        return cshhw;
    }

    public void setCshhw(Double cshhw) {
        this.cshhw = cshhw;
    }

    public Double getZf() {
        return zf;
    }

    public void setZf(Double zf) {
        this.zf = zf;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFood_name() { return food_name; }

    public void setFood_name(String food_name) { this.food_name = food_name; }

    public Double getFoodG() { return foodG; }

    public void setFoodG(Double foodG) { this.foodG = foodG; }

    public Double getRl() { return rl; }

    public void setRl(Double rl) { this.rl = rl; }

    public Double getEnergy() { return energy; }

    public void setEnergy(Double energy) { this.energy = energy; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public Boolean getComplete() { return isComplete; }

    public void setComplete(Boolean complete) { isComplete = complete; }

    public String getKind() { return kind; }

    public void setKind(String kind) { this.kind = kind; }
}
