package com.example.a.dietplanning.question;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.a.dietplanning.R;
import com.example.a.dietplanning.datepicker.CustomDatePicker;
import com.example.a.dietplanning.datepicker.DateFormatUtils;
import com.example.a.dietplanning.db.User;

public class InfoActivity extends AppCompatActivity {
    private Button nextButton;//下一步按钮
    private RadioGroup sexRadio;//性别单选组
    private RadioButton sexRadioButton ;//选中的单选按钮
    private EditText userBtd;//用户的出生年份编辑框
    private CustomDatePicker mDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //获取各控件
        nextButton=(Button) findViewById(R.id.next1);
        sexRadio=(RadioGroup) findViewById(R.id.radio_sex);
        userBtd=(EditText) findViewById(R.id.user_btd);
        userBtd.setInputType(InputType.TYPE_NULL);
        //初始化日期编辑器
        initDatePicker();
        //设置button为不可点击状态
        nextButton.setEnabled(false);
        nextButton.setAlpha(0.5f);//设置按钮的透明度
        //为下一步按钮设置点击事件
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String date=userBtd.getText().toString();
                    String birthday[]=date.split("-");
                    User.user.getUserBirthday().setYear(Integer.parseInt(birthday[0]));//设置用户的出生年份
                    User.user.getUserBirthday().setMonth(Integer.parseInt(birthday[1]));//设置用户的出生月份
                    User.user.getUserBirthday().setDay(Integer.parseInt(birthday[2]));//设置用户的出生日
                    //Log.v("1"," "+userBtdYear.getText().toString()+","+userBtdMonth.getText().toString()+","+userBtdDay.getText().toString());
                    Intent intent=new Intent(InfoActivity.this,BMIActivity.class);
//                    //将用户数据传递至新的Activity
//                    intent.putExtra("user_data",user);
                    //开启新的activity，进入下一个问题页面
                    startActivity(intent);
                }catch(Exception ex){
                    Toast.makeText(InfoActivity.this, "请输入正确的日期", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //为secRadio设置监听器
        sexRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                nextButton.setEnabled(true);//设置下一步按钮可点击
                nextButton.setAlpha(1.0f);//设置按钮的透明度
                sexRadioButton=(RadioButton) findViewById(i);
                //设置用户的性别
                User.user.setSex(sexRadioButton.getText().toString());
            }
        });
        //为生日编辑框设置点击事件
        userBtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatePicker.show(userBtd.getText().toString());
            }
        });
        userBtd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    mDatePicker.show(userBtd.getText().toString());
                }
            }
        });
    }
    //初始化日期选择器
    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("1940-01-01", false);
        long endTimestamp = System.currentTimeMillis();

        userBtd.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                userBtd.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }
}
