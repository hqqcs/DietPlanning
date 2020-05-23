package com.example.a.dietplanning;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a.dietplanning.activity.BodyDataActivity;
import com.example.a.dietplanning.activity.DietPlaningActivity;
import com.example.a.dietplanning.activity.DietRecordActivity;
import com.example.a.dietplanning.activity.DietSortActivity;
import com.example.a.dietplanning.activity.SearchFoodActivity;
import com.example.a.dietplanning.activity.SetActivity;
import com.example.a.dietplanning.adapter.FoodAdapter;
import com.example.a.dietplanning.db.Nutriment;
import com.example.a.dietplanning.db.NutrimentInfo;
import com.example.a.dietplanning.db.RecommendFood;
import com.example.a.dietplanning.utils.GetBeforeData;
import com.example.a.dietplanning.utils.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static com.example.a.dietplanning.utils.MyApplication.getContext;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Integer> imageRes=new ArrayList<>();//轮播图的图片资源
    private List<String> titles=new ArrayList<>();//轮播图的标题
    private Boolean isEvaluate;
    private List<RecommendFood> breakFasts;//早餐列表
    private List<RecommendFood> lunchs;//午餐列表
    private List<RecommendFood> suppers;//晚餐列表
    public static int i=0,j=0,k=0;
    public static int id=0;//记录现在查看的是哪一餐

    private Banner banner;//播放轮播图的组件
    private Nutriment nutriment;//营养素
    private Button createFood;
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private RecyclerView foodRV;//显示食物的列表
    private FoodAdapter foodAdapter;//适配器

    //三餐导航个组件
    private LinearLayout zaocanLL,wucanLL,wangcanLL;
    private ImageView zaocanIV,wucanIV,wangcanIV;
    private TextView zaocanTV,wucanTV,wangcanTV;

    private LinearLayout noFoodLL,noChangeButtonLL;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_menue);
        }
        //设置默认选中
        navView.setCheckedItem(R.id.body_data);
        //滑动菜单的点击事件
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.body_data:
                        //身体数据
                        Intent bodyDataIntent = new Intent(MainActivity.this,BodyDataActivity.class);
                        startActivity(bodyDataIntent);
                        break;
                    case R.id.diet_record:
                        //饮食记录
                        Intent dietRecordIntent = new Intent(MainActivity.this,DietRecordActivity.class);
                        startActivity(dietRecordIntent);
                        break;
                    case R.id.diet_sort:
                        //饮食评分
                        Intent dietSortIntent = new Intent(MainActivity.this,DietSortActivity.class);
                        startActivity(dietSortIntent);
                        break;
                    case R.id.set:
                        //设置
                        Intent setIntent = new Intent(MainActivity.this,SetActivity.class);
                        startActivity(setIntent);
                        break;
                }
                return true;
            }
        });
        initBannerData();
        initBanner();
        initNavigationView();
        initComponent();
        initRecycleView();
        zaocanLL.setOnClickListener(this);
        wucanLL.setOnClickListener(this);
        wangcanLL.setOnClickListener(this);
        createFood.setOnClickListener(this);
        if (id==0){
            zaocanTV.setTextColor(getResources().getColor(R.color.colorFood));
            zaocanIV.setImageResource(R.drawable.zaocan_after);
        }
        if (id==1){
            wucanIV.setImageResource(R.drawable.wucan_after);
            wucanTV.setTextColor(getResources().getColor(R.color.colorFood));
        }
        if (id==2){
            wangcanIV.setImageResource(R.drawable.wancan_after);
            wangcanTV.setTextColor(getResources().getColor(R.color.colorFood));
        }
//        isEvaluate=prefs.getBoolean("isEvaluate",false);
//        getPrefs();
//        recommendFoods=DataSupport.where("data=?",GetBeforeData.getBeforeData(null,0).get(0)).find(RecommendFood.class);
//        if(breakFasts.size()==0){
//            noFoodView();
//        }
//        else{
//            haveFoodView();
//            if(id==0){
//                foodAdapter=new FoodAdapter(breakFasts);
//                foodRV.setAdapter(foodAdapter);
//            }
//            if (id==1){
//                foodAdapter = new FoodAdapter(lunchs);
//                foodRV.setAdapter(foodAdapter);
//            }
//            if (id==2){
//                foodAdapter = new FoodAdapter(suppers);
//                foodRV.setAdapter(foodAdapter);
//            }
//        }
    }

    /**
     * 初始化轮播图的数据
     */
    private void initBannerData() {
        imageRes.add(R.drawable.zhifang);
        imageRes.add(R.drawable.dangbaizhi);
        imageRes.add(R.drawable.tanshuihuahewu);
        imageRes.add(R.drawable.reliang);

//        prefs=PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
//        if(prefs.getBoolean("isEvaluate",false)){
//            NutrimentInfo nutrimentInfo=DataSupport.where("data=?",GetBeforeData.getBeforeData(null,0).get(0)).find(NutrimentInfo.class).get(0);
//            if(nutrimentInfo!=null){
//                titles.add("建议每天摄入脂肪："+nutrimentInfo.getFat()+'g');
//                titles.add("建议每天摄入的蛋白质："+nutrimentInfo.getProtein()+"g");
//                titles.add("建议每天摄入的碳水化合物："+nutrimentInfo.getCarbohydrate()+"g");
//                titles.add("建议每天摄入的热量："+nutrimentInfo.getHeat()+"千卡");
//            }
//        }
//        else{
//            nutriment=new Gson().fromJson(prefs.getString("nutriment",null),Nutriment.class);
//            if(nutriment!=null){
//                titles.add("建议每天摄入脂肪："+nutriment.getFat()+'g');
//                titles.add("建议每天摄入的蛋白质："+nutriment.getProtein()+"g");
//                titles.add("建议每天摄入的碳水化合物："+nutriment.getCarbohydrate()+"g");
//                titles.add("建议每天摄入的热量："+nutriment.getHeat()+"千卡");
        titles.add("建议每天摄入脂肪：80g");
        titles.add("建议每天摄入的蛋白质：60g");
        titles.add("建议每天摄入的碳水化合物：185g");
        titles.add("建议每天摄入的热量：1400千卡");
 //           }
  //      }
    }

    /**
     * 初始化列表的适配器
     */
    private void initRecycleView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        foodRV.setLayoutManager(layoutManager);
    }

    /**
     * 初始化各组件
     */
    private void initComponent() {
        foodRV=findViewById(R.id.food_list);
        createFood=findViewById(R.id.create_food);
        noFoodLL=findViewById(R.id.no_food);
        noChangeButtonLL=findViewById(R.id.change_LL);
    }

    /**
     * 初始化三餐的导航栏
     */
    private void initNavigationView() {
        zaocanLL=findViewById(R.id.zaocan);wucanLL=findViewById(R.id.wucan);wangcanLL=findViewById(R.id.wancan);
        zaocanIV=findViewById(R.id.zaocan_image);wucanIV=findViewById(R.id.wucan_image);wangcanIV=findViewById(R.id.wancan_image);
        zaocanTV=findViewById(R.id.zaocan_text);wucanTV=findViewById(R.id.wucan_text);wangcanTV=findViewById(R.id.wancan_text);
    }

    /**
     * 初始化轮播图
     */
    private void initBanner() {
        banner=findViewById(R.id.banner_food);
        banner.setImageLoader(new com.youth.banner.loader.ImageLoader(){

            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageView.setImageResource((Integer)path);
            }
        });
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);//设置圆形指示器与标题
        banner.setIndicatorGravity(BannerConfig.RIGHT);//设置指示器位置
        banner.setDelayTime(3000);//设置轮播时间
        banner.setImages(imageRes);//设置图片源
        banner.setBannerTitles(titles);//设置标题源
        banner.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                //跳转到搜索界面
                Intent intent=new Intent(MainActivity.this,SearchFoodActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }/**
     * 获取SharedPreferences中的食物数据
     */
    private void getPrefs(){
        breakFasts=new ArrayList<>();
        lunchs=new ArrayList<>();
        suppers=new ArrayList<>();
        String breakFast=prefs.getString("breakFast",null);
        String lunch=prefs.getString("lunch",null);
        String supper=prefs.getString("supper",null);
//        breakFast2=prefs.getString("breakFast2",null);
//        lunch2=prefs.getString("lunch2",null);
//        supper2=prefs.getString("supper2",null);
        if(breakFast!=null&&lunch!=null&&supper!=null){
            Gson gson=new Gson();
            breakFasts=gson.fromJson(breakFast,new TypeToken<List<RecommendFood>>(){}.getType());
            lunchs=gson.fromJson(lunch,new TypeToken<List<RecommendFood>>(){}.getType());
            suppers=gson.fromJson(supper,new TypeToken<List<RecommendFood>>(){}.getType());
        }
    }

    /**
     * 设置没有推荐食物时界面的显示
     */
    public void noFoodView(){
        //显示没有推荐食物的布局
        noFoodLL.setVisibility(View.VISIBLE);
        //隐藏有食物的布局
        noChangeButtonLL.setVisibility(View.GONE);
        foodRV.setVisibility(View.GONE);
    }

    /**
     * 设置有食物的布局
     */
    public void haveFoodView(){
        //隐藏没有推荐食物的布局
        noFoodLL.setVisibility(View.GONE);
        //显示有食物的布局
        noChangeButtonLL.setVisibility(View.VISIBLE);
        foodRV.setVisibility(View.VISIBLE);
    }

    /**
     * 当选中其中一个选项卡时，其他选项卡重置为默认
     */
    private void clearChioce() {
        zaocanIV.setImageResource(R.drawable.zaocan_before);
        wucanIV.setImageResource(R.drawable.wucan_before);
        wangcanIV.setImageResource(R.drawable.wancan_before);
        zaocanTV.setTextColor(getResources().getColor(R.color.colorGray));
        wucanTV.setTextColor(getResources().getColor(R.color.colorGray));
        wangcanTV.setTextColor(getResources().getColor(R.color.colorGray));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zaocan:
                clearChioce();
                zaocanIV.setImageResource(R.drawable.zaocan_after);
                zaocanTV.setTextColor(getResources().getColor(R.color.colorFood));
                if(breakFasts.size()!=0){
                    foodAdapter=new FoodAdapter(breakFasts);
                    foodRV.setAdapter(foodAdapter);
                }
                id=0;
                break;
            case R.id.wucan:
                clearChioce();
                wucanIV.setImageResource(R.drawable.wucan_after);
                wucanTV.setTextColor(getResources().getColor(R.color.colorFood));
                if (lunchs.size()!=0) {
                    foodAdapter = new FoodAdapter(lunchs);
                    foodRV.setAdapter(foodAdapter);
                }
                id=1;
                break;
            case R.id.wancan:
                clearChioce();
                wangcanIV.setImageResource(R.drawable.wancan_after);
                wangcanTV.setTextColor(getResources().getColor(R.color.colorFood));
                if (suppers.size()!=0){
                    foodAdapter=new FoodAdapter(suppers);
                    foodRV.setAdapter(foodAdapter);
                }
                id=2;
                break;
            case R.id.create_food:
                //跳转到食物选择界面
                Intent intent = new Intent(MainActivity.this,DietPlaningActivity.class);
                startActivityForResult(intent,1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    //将返回的食物数据显示到列表中
                }
                break;
            default:
                break;
        }
    }
}
