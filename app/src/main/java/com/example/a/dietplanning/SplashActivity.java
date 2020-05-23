package com.example.a.dietplanning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a.dietplanning.activity.LoginActivity;
import com.example.a.dietplanning.question.TargetActivity;

/**
 * 启动页面
 * 判断用户应进入那个界面
 */
public class SplashActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    protected int splashTime = 1000;
    private Thread splashThread;
    private TextView textView;

    private boolean isFirst;//是否第一次登陆
    private boolean isEvaluate;//是否已经测评

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //解决启动前白屏现象
        setTheme(R.style.AppTheme_Launcher1);
        //实现全屏效果
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textView = findViewById(R.id.title);

        /**
         * 设置字体样式
         */
        //得到AssetManager
        AssetManager mgr = getAssets();
        //根据路径得到Typeface
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/comic sans ms.ttf");
        //设置字体
        textView.setTypeface(tf);

        preferences = getSharedPreferences("splash",MODE_PRIVATE);
        editor = preferences.edit();


        splashThread = new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    synchronized (this){
                        wait(splashTime);
                    }
                }catch (InterruptedException e){}
                finally {
                    finish();
                    //判断进入哪个界面
                    //test
                    Intent intent = new Intent();
                    isFirst = preferences.getBoolean("isFirst",true);
                    isEvaluate = preferences.getBoolean("isEvaluate",false);
                    if(isFirst){
                        intent = new Intent(SplashActivity.this,LoginActivity.class);
                    }
                    else if(!isFirst && isEvaluate){
                        intent = new Intent(SplashActivity.this,MainActivity.class);
                    }
                    else if(!isFirst && !isEvaluate){
                        intent = new Intent(SplashActivity.this,TargetActivity.class);
                    }
                    startActivity(intent);
                }
            }
        };
        splashThread.start();
    }
}
