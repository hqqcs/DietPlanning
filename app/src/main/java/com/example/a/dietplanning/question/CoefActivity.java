package com.example.a.dietplanning.question;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.a.dietplanning.MainActivity;
import com.example.a.dietplanning.R;
import com.example.a.dietplanning.activity.ActivityCollector;
import com.example.a.dietplanning.db.User;
import com.example.a.dietplanning.service.FoodService;
import com.example.a.dietplanning.utils.ToastUtil;
import com.google.gson.Gson;

public class CoefActivity extends AppCompatActivity {
    public static Handler handler;//消息处理
    private Button submitButton;//完成按钮
    private RadioGroup coefRadio;//判断用户运动目标的单选按钮组
    private ProgressDialog progressDialog;//进度对话框
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coef);
        handler=new MessageUtil();

        //获取相应的控件
        submitButton=(Button) findViewById(R.id.next_coef);
        coefRadio=(RadioGroup)findViewById(R.id.radio_coef);

        //设置按钮为不可点击
        submitButton.setEnabled(false);
        submitButton.setAlpha(0.5f);//设置按钮的透明度
        editor=PreferenceManager.getDefaultSharedPreferences(CoefActivity.this).edit();

        //为下一步按钮设置点击事件
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                //将用户输入的数据信息持久化
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String userData=new Gson().toJson(User.user);
                        editor.putString("user_data",userData);
                        editor.apply();
                    }
                }).start();
                Intent intent=new Intent(CoefActivity.this,FoodService.class);
                //开启服务向服务器发出请求
                startService(intent);
            }
        });

        //为单选按钮添加点击事件
        coefRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                submitButton.setEnabled(true);
                submitButton.setAlpha(1.0f);//设置按钮的透明度
                switch (i) {
                    case R.id.radio_coef1:
                    case R.id.radio_coef2:
                        User.user.setCoef(0.2);//如果用户选择了第1/2项，则将用户的运动目标设置为0.2（减脂）
                        break;
                    case R.id.radio_coef3:
                        User.user.setCoef(0.3);//如果用户选择了第3项，则将用户的心肺功能设置为0.3（增肌）
                        break;
                    case R.id.radio_coef4:
                        User.user.setCoef(0.4);//如果用户选择了第4项，则将用户的心肺功能设置为0.4（保持健康）
                        break;
                    case R.id.radio_coef5:
                        User.user.setCoef(0.5);//如果用户选择了第5项，则将用户的心肺功能设置为0.5（保持健康）
                        break;
                    default:
                        break;
                }
            }
        });
    }
    /**
     * 消息处理内部类
     */
    private class MessageUtil extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x005){
                //获得消息中的数据赋值给用户计划实例
                Bundle data = msg.getData();
                final String foodStr=data.getString("food");//人体每天需要摄入的营养素
                Log.v("1",foodStr);
                Intent intent=new Intent(CoefActivity.this,MainActivity.class);//成功获取服务器传递的计划数据，跳转到主界面
                startActivity(intent);
                editor.putString("nutriment",foodStr);
                editor.putBoolean("isFirst",false);
                editor.apply();
                ActivityCollector.finishAll();//销毁之前的活动
                closeProgressDialog();
            }
            else if(msg.what==0x006){
                Bundle data = msg.getData();
                String str = data.getString("food");
                ToastUtil.showToast(CoefActivity.this,str);
                closeProgressDialog();
            }
        }
    }
    /**
     * 显示进度对话框
     */
    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("请稍等...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}

