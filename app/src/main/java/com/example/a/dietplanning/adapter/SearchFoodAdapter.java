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
import com.example.a.dietplanning.db.Food;
import com.example.a.dietplanning.utils.VariableUtil;
import com.google.gson.Gson;

import java.util.List;

/**
 * 显示搜索的食物的适配器
 */
public class SearchFoodAdapter extends RecyclerView.Adapter<SearchFoodAdapter.ViewHolder>{

    private List<Food> foodList;//数据源

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView foodName;//食物名称
        TextView foodNengliang;//食物能量
        ImageView foodImage;//食物图片
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            foodImage=itemView.findViewById(R.id.search_food_photo);
            foodName=itemView.findViewById(R.id.search_food_name);
            foodNengliang=itemView.findViewById(R.id.search_food_nengliang);
            view=itemView;
        }
    }
    public SearchFoodAdapter(List<Food> foods){
        foodList=foods;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.search_food_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Food food=foodList.get(position);
                Intent intent=new Intent(holder.view.getContext(),SearchFoodDetailActivity.class);
                Gson gson=new Gson();
                String foodstr=gson.toJson(food);
                intent.putExtra("food_detail",foodstr);
                holder.view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food=foodList.get(position);
        holder.foodName.setText(food.getFood_name().split("，")[0]);
        String url=VariableUtil.Service_IP+"food/"+food.getFood_name()+".jpg";
        Glide.with(holder.view.getContext()).load(url).into(holder.foodImage);
        holder.foodNengliang.setText(food.getRl()+"千卡/100g(毫升)");
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
