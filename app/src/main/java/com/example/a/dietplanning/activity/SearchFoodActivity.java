
package com.example.a.dietplanning.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;

import com.example.a.dietplanning.R;
import com.example.a.dietplanning.adapter.SearchFoodAdapter;
import com.example.a.dietplanning.db.Food;
import com.example.a.dietplanning.service.SearchFoodService;
import com.example.a.dietplanning.utils.ChangeColor;
import com.example.a.dietplanning.utils.DensityUtil;
import com.example.a.dietplanning.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SearchFoodActivity extends BaseActivity {
    private List<Food> foodList;//数据源

    public static Handler mHandler;

    private SearchView foodSearchView;
    private RecyclerView foodRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);mHandler=new MessageUtil();
        foodList=new ArrayList<>();
        ChangeColor.changeColor(this,Color.parseColor("#24C68A"));
        initComponent();
        initSearchAdapter();
        setButtonOnclickListening();
    }
    /**
     * 初始化各组件
     */
    private void initComponent(){
        foodSearchView=(SearchView) findViewById(R.id.search_food);
        foodRV=(RecyclerView) findViewById(R.id.food_search_RV);
        //消除搜索组件的下划线
        View viewById = foodSearchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        if (viewById != null) {
            viewById.setBackgroundColor(Color.TRANSPARENT);
        }
        DensityUtil.setIcon(foodSearchView,"搜索食物",null);
    }

    /**
     * 为RecycleView设置数据和适配器
     */
    private void initSearchAdapter(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        foodRV.setLayoutManager(linearLayoutManager);
        SearchFoodAdapter foodAdapter=new SearchFoodAdapter(foodList);
        foodRV.setAdapter(foodAdapter);
    }

    /**
     * 设置按钮的点击事件
     */
    private void setButtonOnclickListening(){
        foodSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText)){
                    Intent intent=new Intent(SearchFoodActivity.this,SearchFoodService.class);
                    intent.putExtra("food_name",newText);
                    startService(intent);
                }
                return false;
            }
        });
    }

    /**
     * 内部类消息处理类
     */
    private class MessageUtil extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x009){
                Bundle data = msg.getData();
                String str=data.getString("food_detail");
                Gson gson=new Gson();
                foodList=gson.fromJson(str,new TypeToken<List<Food>>(){}.getType());
                //重新设置RecycleView的数据
                SearchFoodAdapter foodAdapter=new SearchFoodAdapter(foodList);
                foodRV.setAdapter(foodAdapter);
            }
            else if(msg.what==0x010){
                Bundle data = msg.getData();
                String str=data.getString("food_detail");
                ToastUtil.showToast(SearchFoodActivity.this,str);
            }
        }
    }
}
