package com.example.a.dietplanning.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a.dietplanning.R;
import com.example.a.dietplanning.db.Nutriment;
import com.example.a.dietplanning.db.NutrimentInfo;
import com.example.a.dietplanning.db.RecommendFood;
import com.example.a.dietplanning.utils.ChangeColor;
import com.example.a.dietplanning.utils.GetBeforeData;
import com.example.a.dietplanning.utils.MyApplication;
import com.example.a.dietplanning.view.ScoreView;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.List;

public class DietSortActivity extends BaseActivity {
    private ScoreView scoreView;
    private TextView scoreTv,recommend_dbz,actual_dbz,recommend_rl,actual_rl,recommend_tsw,actual_tsw,recommend_zf,actual_zf;
    private double rec_dbz=0.0,rec_rl=0.0,rec_tsw=0.0,rec_zf=0.0,ac_dbz=0.0,ac_rl=0.0,ac_tsw=0.0,ac_zf=0.0;
    private Button foodBtn;
    private SharedPreferences prefs;
    private Nutriment nutriment;//推荐营养素
    private List<RecommendFood> recommendFoods;//饮食记录
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_sort);
        prefs=PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        initComponent();
        setData();
        setOnClickListeners();
        ChangeColor.changeColor(this,Color.parseColor("#3e424e"));
    }/**
     * 为组件设置点击事件
     */
    private void setOnClickListeners() {
        foodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DietSortActivity.this,DietRecordActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 为组件设置数据
     */
    private void setData() {
        DecimalFormat df = new DecimalFormat("0.0");
        String nutrimentStr=prefs.getString("nutriment",null);
        if (nutrimentStr!=null){
            nutriment=new Gson().fromJson(nutrimentStr,Nutriment.class);
            rec_dbz=nutriment.getProtein();
            rec_rl=nutriment.getHeat();
            rec_tsw=nutriment.getCarbohydrate();
            rec_zf=nutriment.getFat();
        }
        recommendFoods = DataSupport.where("data=?",GetBeforeData.getBeforeData(null,0).get(0)).find(RecommendFood.class);
        for(RecommendFood recommendFood:recommendFoods){
            double x=recommendFood.getFoodG()/100;
            ac_rl+=recommendFood.getRl();
            ac_dbz+=recommendFood.getDbz()*x;
            ac_tsw+=recommendFood.getCshhw()*x;
            ac_zf+=recommendFood.getZf()*x;
        }
        int value=100-(int)((Math.abs(ac_zf-rec_zf)/rec_zf+Math.abs(ac_dbz-rec_dbz)/rec_dbz+Math.abs(ac_tsw-rec_tsw)/rec_tsw+Math.abs(ac_rl-rec_rl)/rec_rl)/4);
        scoreView.setValueDuringRefresh(value,100);
        scoreTv.setText(value+"");
        recommend_dbz.setText(rec_dbz+"");
        actual_dbz.setText(ac_dbz+"");
        recommend_rl.setText(rec_rl+"");
        actual_rl.setText(ac_rl+"");
        recommend_tsw.setText(rec_tsw+"");
        actual_tsw.setText(ac_tsw+"");
        recommend_zf.setText(rec_zf+"");
        actual_zf.setText(ac_zf+"");
    }

    /**
     * 初始化各组件
     */
    private void initComponent() {
        scoreView=(ScoreView)findViewById(R.id.score_view);
        scoreTv=(TextView)findViewById(R.id.score);
        foodBtn=(Button)findViewById(R.id.food_btn);
        recommend_dbz=(TextView)findViewById(R.id.recommend_dbz);
        actual_dbz=(TextView)findViewById(R.id.actual_dbz);
        recommend_rl=(TextView)findViewById(R.id.recommend_rl);
        actual_rl=(TextView)findViewById(R.id.actual_rl);
        recommend_tsw=(TextView)findViewById(R.id.recommend_tsw);
        actual_tsw=(TextView)findViewById(R.id.actual_tsw);
        recommend_zf=(TextView)findViewById(R.id.recommend_zf);
        actual_zf=(TextView)findViewById(R.id.actual_zf);
    }

}
