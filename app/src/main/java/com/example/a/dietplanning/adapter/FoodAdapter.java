package com.example.a.dietplanning.adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a.dietplanning.R;
import com.example.a.dietplanning.activity.SearchFoodDetailActivity;
import com.example.a.dietplanning.db.RecommendFood;
import com.example.a.dietplanning.utils.GetBeforeData;
import com.example.a.dietplanning.utils.MyApplication;
import com.example.a.dietplanning.utils.VariableUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private List<RecommendFood> recommendFoods;
    private SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
    public FoodAdapter(List<RecommendFood> recommendFoods){
        this.recommendFoods=recommendFoods;
    }
    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecommendFood recommendFood=recommendFoods.get(holder.getAdapterPosition());
                Gson gson=new Gson();
                String foodStr=gson.toJson(recommendFood);
                Intent intent=new Intent(view.getContext(),SearchFoodDetailActivity.class);
                intent.putExtra("food_detail",foodStr);
                view.getContext().startActivity(intent);
            }
        });
        holder.clockInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.clockInBtn.setClickable(false);
                holder.clockInBtn.setText("完成");
                holder.clockInBtn.setAlpha(0.5f);
                RecommendFood recommendFood=recommendFoods.get(holder.getAdapterPosition());
                String kind=recommendFood.getKind();
                String foodStr;
                if (kind.equals("早餐")){
                    foodStr=prefs.getString("breakFast",null);
                }
                else if (kind.equals("午餐")){
                    foodStr=prefs.getString("lunch",null);
                }
                else{
                    foodStr=prefs.getString("supper",null);
                }
                Gson gson=new Gson();
                List<RecommendFood> foods=gson.fromJson(foodStr,new TypeToken<List<RecommendFood>>(){}.getType());
                foods.get(holder.getAdapterPosition()).setComplete(true);
                foodStr=gson.toJson(foods);
                SharedPreferences.Editor editor=prefs.edit();
                if (kind.equals("早餐")){
                    editor.putString("breakFast",foodStr);
                }
                else if (kind.equals("午餐")){
                    editor.putString("lunch",foodStr);
                }
                else{
                    editor.putString("supper",foodStr);
                }
                editor.apply();
                recommendFood.setComplete(true);
                recommendFood.setData(GetBeforeData.getBeforeData(null,0).get(0));
                recommendFood.save();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {
        RecommendFood recommendFood=recommendFoods.get(position);
        holder.foodNaame.setText(recommendFood.getFood_name().split("，")[0]);
        holder.foodNengLiang.setText(recommendFood.getEnergy());
        holder.foodG.setText(recommendFood.getFoodG());
        String url=VariableUtil.Service_IP+"food/"+recommendFood.getFood_name()+".jpg";
        Glide.with(holder.view.getContext()).load(url).into(holder.foodImage);
        if (recommendFood.getComplete()){
            holder.clockInBtn.setClickable(false);
            holder.clockInBtn.setText("完成");
            holder.clockInBtn.setAlpha(0.5f);
        }
        else {
            holder.clockInBtn.setClickable(true);
            holder.clockInBtn.setText("打卡");
            holder.clockInBtn.setAlpha(1.0f);
        }
    }

    @Override
    public int getItemCount() {
        return recommendFoods.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodNaame,foodNengLiang,foodG;
        ImageView foodImage;
        Button clockInBtn;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            foodNaame=itemView.findViewById(R.id.food_name);
            foodNengLiang=itemView.findViewById(R.id.food_nengliang);
            foodG=itemView.findViewById(R.id.food_weight);
            foodImage=itemView.findViewById(R.id.food_image);
            clockInBtn=itemView.findViewById(R.id.clock_in_btn);
        }
    }
}
