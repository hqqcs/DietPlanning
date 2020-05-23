package com.example.a.dietplanning.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class LineChartManager {
    private LineChart lineChart;
    private YAxis leftAxis;
//    private YAxis rightAxis;
    private XAxis xAxis;
    private LineData lineData;
    private LineDataSet lineDataSet;
    private List<LineDataSet> lineDataSets=new ArrayList<>();
//    private SimpleDateFormat df =new SimpleDateFormat("HH:mm:ss");//设置日期格式
    private List<String> timeList=new ArrayList<>();//存储x轴的时间

    //一条曲线
    public LineChartManager(LineChart mLineChart, String name, int color){
        this.lineChart=mLineChart;
        leftAxis=lineChart.getAxisLeft();
//        rightAxis=lineChart.getAxisRight();
        xAxis=lineChart.getXAxis();
        initLineChart();
        initLineDataSet(name,color);
    }

    //多条曲线
    public LineChartManager(LineChart mLineChart, List<String> names, List<Integer> colors){
        this.lineChart=mLineChart;
        leftAxis=lineChart.getAxisLeft();
//        rightAxis=lineChart.getAxisRight();
        xAxis=lineChart.getXAxis();
        initLineChart();
        initLineDataSet(names,colors);
    }

    /**
     * 初始化LineChar
     */
    private void initLineChart() {
        lineChart.setDrawGridBackground(false);//是否显示表格颜色
        lineChart.setDrawBorders(false);//显示边界
        lineChart.setNoDataText("暂无数据");   // 没有数据时样式
        Description description = new Description();  // 这部分是深度定制描述文本，颜色，字体等
        description.setText("");
        lineChart.setDescription(description);
        lineChart.getAxisRight().setEnabled(false);    // 不绘制右侧的轴线
        lineChart.getAxisLeft().setEnabled(false);    // 不绘制左侧的轴线
        Legend legend=lineChart.getLegend();//折线图标签设置
        legend.setForm(Legend.LegendForm.LINE);//用线表示
        legend.setTextSize(11f);
        legend.setTextColor(Color.parseColor("#ffffff"));
//        //设置显示位置
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);;
//        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//        legend.setDrawInside(false);

        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);//缩放的时候有用，比如放大的时候，不想把横坐标的日期再细分
        xAxis.setLabelCount(10);//强制有多少个刻度
        xAxis.setDrawGridLines(false);   // 是否绘制网格线，默认true
        xAxis.setTextColor(Color.parseColor("#ffffff"));
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return timeList.get((int)value%timeList.size());
            }
        });

        leftAxis.setDrawGridLines(false);
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(Color.parseColor("#ffffff"));
//        rightAxis.setAxisMinimum(0f);

    }

    /**
     * 初始化折线（一条线）
     * @param name
     * @param color
     */
    private void initLineDataSet(String name, int color) {
        lineDataSet=new LineDataSet(null,name);
        // 显示坐标点的小圆点
        lineDataSet.setDrawCircles(true);
        // 定位线
        lineDataSet.setHighlightEnabled(true);
        // 线宽
        lineDataSet.setLineWidth(2.0f);
        // 显示的圆形大小
        lineDataSet.setCircleSize(4f);
        // 显示颜色
        lineDataSet.setColor(color);
        // 圆形的颜色
        lineDataSet.setCircleColor(color);
        // 高亮的线的颜色
        lineDataSet.setHighLightColor(color);
        // 设置坐标点的颜色
        lineDataSet.setFillColor(color);
        // 设置坐标点为空心环状
        lineDataSet.setDrawCircleHole(false);
        //坐标轴在左侧
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);//坐标轴在左侧
        //坐标轴值的字体大小
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setValueTextColor(Color.parseColor("#ffffff"));
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //添加一个空的LineData
        lineData=new LineData();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    /**
     * 初始化折线（多条线）
     * @param names
     * @param colors
     */
    private void initLineDataSet(List<String> names, List<Integer> colors) {
        for(int i=0;i<names.size();i++){
            lineDataSet = new LineDataSet(null, names.get(i));
            // 显示坐标点的小圆点
            lineDataSet.setDrawCircles(true);
            // 定位线
            lineDataSet.setHighlightEnabled(true);
            // 线宽
            lineDataSet.setLineWidth(2.0f);
            // 显示的圆形大小
            lineDataSet.setCircleSize(4f);
            // 显示颜色
            lineDataSet.setColor(colors.get(i));
            // 圆形的颜色
            lineDataSet.setCircleColor(colors.get(i));
            // 高亮的线的颜色
            lineDataSet.setHighLightColor(colors.get(i));
            // 设置坐标点的颜色
            lineDataSet.setFillColor(colors.get(i));
            // 设置坐标点为空心环状
            lineDataSet.setDrawCircleHole(false);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setMode(LineDataSet.Mode.LINEAR);
            lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            lineDataSet.setValueTextSize(10f);
            lineDataSet.setValueTextColor(Color.parseColor("#ffffff"));
            lineDataSets.add(lineDataSet);
        }
        //添加一个空的 LineData
        lineData=new LineData();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    /**
     * 动态添加数据（一条折线图）
     *
     * @param number
     */
    public void addEntry(int number,String str) {
        //最开始的时候才添加 lineDataSet（一个lineDataSet 代表一条线）
        if (lineDataSet.getEntryCount() == 0) {
            lineData.addDataSet(lineDataSet);
        }
        lineChart.setData(lineData);
//        //避免集合数据过多，及时清空（做这样的处理，并不知道有没有用，但还是这样做了）
//        if (timeList.size() > 11) {
//            timeList.clear();
//        }

        timeList.add(str);
        Entry entry = new Entry(lineDataSet.getEntryCount(), number);
        //通知数据已经改变
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        //设置在曲线图中显示的最大数量
        lineChart.setVisibleXRangeMaximum(10);
        //移到某个位置
        lineChart.moveViewToX(lineData.getEntryCount() - 5);
    }
    /**
     * 动态添加数据（多条折线图）
     *
     * @param numbers
     */
    public void addEntry(List<Float> numbers, String str) {
        if (lineDataSets.get(0).getEntryCount() == 0) {
            lineData = new LineData(lineDataSets.get(0),lineDataSets.get(1));
            lineChart.setData(lineData);
        }
//        if (timeList.size() > 11) {
//            timeList.clear();
//        }
        timeList.add(str);
        for (int i = 0; i < numbers.size(); i++) {
            Entry entry = new Entry(lineDataSet.getEntryCount(), numbers.get(i));
            lineData.addEntry(entry, i);
            lineData.notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.setVisibleXRangeMaximum(6);
            lineChart.moveViewToX(lineData.getEntryCount() - 5);
        }
    }
    /**
     * 设置Y轴值
     *
     *
     * @param min
     * @param labelCount
     */
    public void setYAxis( float min, int labelCount) {
//        if (max < min) {
//            return;
//        }
//        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setLabelCount(labelCount, false);

//        rightAxis.setAxisMaximum(max);
//        rightAxis.setAxisMinimum(min);
//        rightAxis.setLabelCount(labelCount, false);
        lineChart.invalidate();
    }

    /**
     * 设置X轴的值
     *
     * @param max
     * @param min
     * @param labelCount
     */
    public void setXAxis(float max, float min, int labelCount) {
        xAxis.setAxisMaximum(max);
        xAxis.setAxisMinimum(min);
        xAxis.setLabelCount(labelCount, true);

        lineChart.invalidate();
    }

    /**
     * 设置描述信息
     *
     * @param str
     */
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        lineChart.setDescription(description);
        lineChart.invalidate();
    }
}
