package com.example.a.dietplanning.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.a.dietplanning.R;
import com.example.a.dietplanning.activity.BaseActivity;
import com.example.a.dietplanning.utils.ChangeColor;

public class SetActivity extends BaseActivity {
    private TextView text1,text2,text3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initComponent();
        setListeners();
        ChangeColor.changeColor(this,Color.parseColor("#584f60"));
    }
    /**
     * 初始化各组件
     */
    private void initComponent() {
        text1=(TextView)findViewById(R.id.text1);
        text2=(TextView)findViewById(R.id.text2);
        text3=(TextView)findViewById(R.id.text3);
    }

    /**
     * 为各组件设置点击事件
     */
    private void setListeners() {
    }
}
