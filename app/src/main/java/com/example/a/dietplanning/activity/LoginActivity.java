package com.example.a.dietplanning.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a.dietplanning.R;
import com.example.a.dietplanning.db.User;
import com.example.a.dietplanning.question.TargetActivity;
import com.example.a.dietplanning.service.LoginService;
import com.example.a.dietplanning.utils.PasswordMD5Util;
import com.example.a.dietplanning.utils.ToastUtil;
import com.example.a.dietplanning.utils.UsernameAndPasswordByIs;

/**
 * 用户登陆界面
 * author：hqq
 * data:2020.4.12
 */
public class LoginActivity extends AppCompatActivity {
    private TextView textView;
    private EditText userNameTv,passwordTv;//用户名和密码
    private TextView newUser;//新用户注册
    private Button loginBtn;//登陆按钮

    public static Handler handler; //消息接收
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        handler = new MessageUtil();
        initComponent();
        setData();
        setListeners();
    }

    /**
     * 初始化各组件
     */
    private void initComponent() {
        //透明状态栏          
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        textView=findViewById(R.id.login_text);
        userNameTv=findViewById(R.id.user_name);
        passwordTv=findViewById(R.id.password);
        newUser=findViewById(R.id.new_user);
        loginBtn=findViewById(R.id.login);
    }

    /**
     * 为各组件设置数据
     */
    private void setData() {
        //得到AssetManager
        AssetManager mgr=getAssets();
        //根据路径得到Typeface
        Typeface tf=Typeface.createFromAsset(mgr, "fonts/comic sans ms.ttf");
        //设置字体
        textView.setTypeface(tf);
        if (getIntent().getStringExtra("user_id")!=null&&getIntent().getStringExtra("password")!=null) {
            userNameTv.setText(getIntent().getStringExtra("user_id"));
            passwordTv.setText(getIntent().getStringExtra("password"));
        }
    }

    /**
     * 为各组件设置点击监听事件
     */
    private void setListeners() {
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //登陆直接
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UsernameAndPasswordByIs.checkusername(userNameTv.getText().toString())&&UsernameAndPasswordByIs.checkPassword(passwordTv.getText().toString())){
                    Intent loginservice = new Intent(LoginActivity.this,LoginService.class);
                    Bundle data = new Bundle();
                    data.putString("user_id",userNameTv.getText().toString());
                    data.putString("password",PasswordMD5Util.generateMD5(passwordTv.getText().toString()));
                    loginservice.putExtras(data);
                    startService(loginservice);
                }else {
                    ToastUtil.showToast(LoginActivity.this,"请输入账户或密码");
                }
            }
        });
    }


    /**
     * 消息处理内部类
     */
    @SuppressLint("HandlerLeak")
    private class MessageUtil extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String str = data.getString("login");
            if(msg.what == 0x003){
                Intent go_main = new Intent(LoginActivity.this,TargetActivity.class);
                User.user.setUserName(userNameTv.getText().toString());
                startActivity(go_main);
                LoginActivity.this.finish();
            }else {
                //获得消息中的数据并显示
                ToastUtil.showToast(LoginActivity.this,str);
            }
        }
    }
}
