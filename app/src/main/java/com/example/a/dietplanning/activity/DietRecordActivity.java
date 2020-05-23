package com.example.a.dietplanning.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a.dietplanning.R;
import com.example.a.dietplanning.adapter.FoodRecordAdapter;
import com.example.a.dietplanning.db.NutrimentInfo;
import com.example.a.dietplanning.db.RecommendFood;
import com.example.a.dietplanning.utils.GetBeforeData;
import com.example.a.dietplanning.utils.LineChartManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DietRecordActivity extends BaseActivity {
    private List<RecommendFood> allRecommendFoods;//所有的饮食数据记录
    private List<RecommendFood> breakFasts;//早餐记录
    private List<RecommendFood> lunchs;//午餐记录‘
    private List<RecommendFood> suppers;//晚餐记录
    private List<NutrimentInfo> nutrimentInfos;
    private List<String> names;//折线名字集合
    private List<Integer> colors;//折线颜色集合
    private List<Float> datas;//数据集合
    private List<String> dates;//日期集合
    private List<Float> values;//纵坐标的数据集合
    private int id=0;//数据的索引

    private RecyclerView breakfastRv,lunchRv,supperRv;
    private TextView dateTv,rlTV,noDataTv;
    private LinearLayout breakfastLL,lunchLL,supperLL;
    private LineChart mLineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_record);
        initComponent();
        setFoodChartData();
        id=dates.size()-1;
        setFoodData();
        initFoodAdapter();
        mLineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() { // 值选择监听器
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                id=(int)e.getX();
                breakFasts.clear();
                lunchs.clear();
                suppers.clear();
                setFoodData();
                initFoodAdapter();
            }
            @Override
            public void onNothingSelected() {
                // 未选中
            }
        });
    }
    /**
     * 设置饮食记录数据
     */
    private void setFoodData() {
        breakFasts=new ArrayList<>();
        lunchs=new ArrayList<>();
        suppers=new ArrayList<>();
        if(id>=0){
            dateTv.setText(dates.get(id));
            for (RecommendFood recommendFood:allRecommendFoods){
                if (recommendFood.getData().equals(nutrimentInfos.get(id).getData())){
                    if(recommendFood.getKind().equals("早餐")){
                        breakFasts.add(recommendFood);
                    }
                    else if (recommendFood.getKind().equals("午餐")){
                        lunchs.add(recommendFood);
                    }
                    else {
                        suppers.add(recommendFood);
                    }
                }
            }
            DecimalFormat df=new DecimalFormat("0.0");
            rlTV.setText(df.format(values.get(id)));
        }
    }

    private void setFoodChartData(){
        allRecommendFoods=new ArrayList<>();
        nutrimentInfos=new ArrayList<>();
        names=new ArrayList<>();
        colors=new ArrayList<>();
        datas=new ArrayList<>();
        names.add("实际摄入");
        names.add("推荐摄入");
        colors.add(getResources().getColor(R.color.colorAccent));
        colors.add(getResources().getColor(R.color.colorGray));
        LineChartManager lineChartManager=new LineChartManager(mLineChart,names,colors);
        lineChartManager.setYAxis(0,10);
        allRecommendFoods=DataSupport.findAll(RecommendFood.class);//找出所有的饮食记录
        nutrimentInfos=DataSupport.findAll(NutrimentInfo.class);
        dates=new ArrayList<>();
        values=new ArrayList<>();
        for(NutrimentInfo nutrimentInfo:nutrimentInfos){
            float value=0;
            String date[]=nutrimentInfo.getData().substring(5,10).split("-");
            dates.add(date[0]+"月"+date[1]+"日");
            for(RecommendFood recommendFood:allRecommendFoods){
                if(recommendFood.getData().equals(nutrimentInfo.getData())){
                    value+=Float.parseFloat(recommendFood.getEnergy().replaceAll("[^0-9.]",""));
                }
            }
            values.add(value);
            if(nutrimentInfo.getData().equals(GetBeforeData.getBeforeData(null,0).get(0))){
                break;
            }
        }
        for(int i=0;i<dates.size();i++){
            datas.add(values.get(i));
            datas.add((float)nutrimentInfos.get(i).getHeat());
            lineChartManager.addEntry(datas,dates.get(i));
            datas.clear();
        }
    }
    /**
     * 为饮食记录recycleView设置适配器
     */
    private void initFoodAdapter() {
        if(breakFasts.size()==0){
            breakfastLL.setVisibility(View.GONE);
        }
        else {
            breakfastLL.setVisibility(View.VISIBLE);
            noDataTv.setVisibility(View.GONE);
            LinearLayoutManager breafastLayoutManager=new LinearLayoutManager(this);
            breafastLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            breakfastRv.setLayoutManager(breafastLayoutManager);
            FoodRecordAdapter adapter=new FoodRecordAdapter(breakFasts);
            breakfastRv.setAdapter(adapter);
        }
        if(lunchs.size()==0){
            lunchLL.setVisibility(View.GONE);
        }
        else {
            lunchLL.setVisibility(View.VISIBLE);
            noDataTv.setVisibility(View.GONE);
            LinearLayoutManager lunchLayoutManger=new LinearLayoutManager(this);
            lunchLayoutManger.setOrientation(LinearLayoutManager.VERTICAL);
            lunchRv.setLayoutManager(lunchLayoutManger);
            FoodRecordAdapter adapter=new FoodRecordAdapter(lunchs);
            lunchRv.setAdapter(adapter);
        }
        if(suppers.size()==0){
            supperLL.setVisibility(View.GONE);
        }
        else {
            supperLL.setVisibility(View.VISIBLE);
            noDataTv.setVisibility(View.GONE);
            LinearLayoutManager supperLayoutManger=new LinearLayoutManager(this);
            supperLayoutManger.setOrientation(LinearLayoutManager.VERTICAL);
            supperRv.setLayoutManager(supperLayoutManger);
            FoodRecordAdapter adapter=new FoodRecordAdapter(suppers);
            supperRv.setAdapter(adapter);
        }
        if (breakFasts.size()==0&&lunchs.size()==0&&suppers.size()==0){
            noDataTv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化各组件
     */
    private void initComponent() {
        mLineChart = (LineChart)findViewById(R.id.day_data);
        breakfastRv=(RecyclerView) findViewById(R.id.breakfast_record);
        lunchRv=(RecyclerView)findViewById(R.id.lunch_record);
        supperRv=(RecyclerView)findViewById(R.id.supper_record);
        breakfastLL=(LinearLayout)findViewById(R.id.breakfast);
        lunchLL=(LinearLayout)findViewById(R.id.lunch);
        supperLL=(LinearLayout)findViewById(R.id.supper);
        dateTv=(TextView)findViewById(R.id.date);
        rlTV=(TextView)findViewById(R.id.heat);
        noDataTv= (TextView)findViewById(R.id.no_data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dates.clear();
        values.clear();
        colors.clear();
        names.clear();
    }
}
