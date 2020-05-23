package com.example.a.dietplanning.question;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.a.dietplanning.R;
import com.example.a.dietplanning.db.User;

public class TargetActivity extends AppCompatActivity {
    private Button nextButton;//下一步按钮
    private RadioGroup STRadio;//判断用户目标的单选按钮组
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        //获取相应的控件
        nextButton=(Button)findViewById(R.id.next_st);
        STRadio=(RadioGroup) findViewById(R.id.radio_sport_target);

        //设置按钮为不可点击
        nextButton.setEnabled(false);
        nextButton.setAlpha(0.5f);//设置按钮的透明度

        //为下一步按钮设置点击事件
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TargetActivity.this,InfoActivity.class);
                //开启新的activity，进入下一个问题页面
                startActivity(intent);
            }
        });

        //为单选按钮添加点击事件
        STRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                nextButton.setEnabled(true);
                nextButton.setAlpha(1.0f);//设置按钮的透明度
                switch (i) {
                    case R.id.radio_sport_target1:
                        User.user.setTarget(1);//如果用户选择了第1项，则将用户的运动目标设置为1（减脂）
                        break;
                    case R.id.radio_sport_target2:
                        User.user.setTarget(2);//如果用户选择了第2项，则将用户的运动目标设置为2（局部塑形）
                        break;
                    case R.id.radio_sport_target3:
                        User.user.setTarget(3);//如果用户选择了第3项，则将用户的运动目标设置为3（增肌）
                        break;
                    case R.id.radio_sport_target4:
                        User.user.setTarget(4);//如果用户选择了第4项，则将用户的运动目标设置为4（保持健康）
                        break;
                    default:
                        break;
                }
            }
        });

    }
}
