package com.example.a.dietplanning.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a.dietplanning.R;
import com.example.a.dietplanning.db.Food;
import com.example.a.dietplanning.utils.VariableUtil;
import com.google.gson.Gson;

/**
 * 搜索的食物详情页
 */
public class SearchFoodDetailActivity extends BaseActivity {
    private Food food=new Food();//食物数据
    private TextView foodName,foodNengLiang,foodRL,foodDBZ,foodTSW,foodZF,foodType;
    private ImageView foodPhoto;//食物图片
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food_detail);
        //获取上一个 activity传递过来的JSON数据
        String str=getIntent().getStringExtra("food_detail");
        //将JSON数据转换成Food对象
        Gson gson=new Gson();
        food=gson.fromJson(str,Food.class);
        initComponent();
        if(food!=null){
            setData();
        }
    }

    /**
     * 初始化各组件
     */
    private void initComponent() {
        foodName=(TextView) findViewById(R.id.detail_food_name);
        foodPhoto=(ImageView) findViewById(R.id.detail_food_photo);
        foodNengLiang=(TextView) findViewById(R.id.detail_food_nengliang);
        foodRL=(TextView) findViewById(R.id.detail_food_rl);
        foodDBZ=(TextView) findViewById(R.id.detail_food_dbz);
        foodTSW=(TextView) findViewById(R.id.detail_food_tsw);
        foodZF=(TextView) findViewById(R.id.detail_food_zf);
        foodType=(TextView) findViewById(R.id.detail_food_type);
    }

    /**
     * 设置各组件的数据
     */
    private void setData() {
        foodName.setText(food.getFood_name().split("，")[0]);
        String url=VariableUtil.Service_IP+"food/"+food.getFood_name()+".jpg";
        Glide.with(this).load(url).into(foodPhoto);
        foodNengLiang.setText(food.getRl()+"千卡/100g(毫升)");
        foodRL.setText(""+food.getRl());
        foodDBZ.setText(""+food.getDbz());
        foodTSW.setText(""+food.getCshhw());
        foodZF.setText(""+food.getZf());
        foodType.setText(""+food.getType());
    }
}
