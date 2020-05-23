package com.example.a.dietplanning.question;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a.dietplanning.R;
import com.example.a.dietplanning.db.User;

public class BMIActivity extends AppCompatActivity {
    private Button nextButton;//下一步按钮
    private EditText userHeight;//记录用户身高的编辑框
    private EditText userWeight;//记录用户体重的编辑框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        //获取各控件
        nextButton=(Button) findViewById(R.id.next2);
        userHeight=(EditText) findViewById(R.id.user_height);
        userWeight=(EditText) findViewById(R.id.user_weight);
        //设置编辑框只能输入整数和小数点
        userHeight.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        userWeight.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //为下一步按钮设置点击事件
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    User.user.setHeight(Double.parseDouble(userHeight.getText().toString()));//获取用户的身高
                    User.user.setWeight(Double.parseDouble(userWeight.getText().toString()));//获取用户的体重
                    Intent intent=new Intent(BMIActivity.this,CoefActivity.class);
                    //开启新的activity，进入下一个问题页面
                    startActivity(intent);
                }catch(Exception ex){
                    Toast.makeText(BMIActivity.this, "请输入正确的身高体重", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
