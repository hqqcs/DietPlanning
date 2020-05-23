package com.example.a.dietplanning.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a.dietplanning.R;
import com.example.a.dietplanning.activity.SearchFoodDetailActivity;
import com.example.a.dietplanning.db.RecommendFood;
import com.example.a.dietplanning.utils.VariableUtil;
import com.google.gson.Gson;

import java.util.List;

public class FoodRecordAdapter extends RecyclerView.Adapter<FoodRecordAdapter.ViewHolder> {
    private List<RecommendFood> recommendFoods;
    public FoodRecordAdapter(List<RecommendFood> recommendFoods){
        this.recommendFoods=recommendFoods;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.food_record_item,parent,false);
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
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecommendFood recommendFood=recommendFoods.get(position);
        holder.foodNaame.setText(recommendFood.getFood_name().split("，")[0]);
        holder.foodNengLiang.setText(recommendFood.getEnergy());
        holder.foodG.setText(recommendFood.getFoodG());
        String url=VariableUtil.Service_IP+"food/"+recommendFood.getFood_name()+".jpg";
        Glide.with(holder.view.getContext()).load(url).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return recommendFoods.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodNaame,foodNengLiang,foodG;
        ImageView foodImage;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            foodNaame=itemView.findViewById( R.id.food_name);
            foodNengLiang=itemView.findViewById(R.id.food_nengliang);
            foodG=itemView.findViewById(R.id.food_weight);
            foodImage=itemView.findViewById(R.id.food_image);
        }
    }
}
