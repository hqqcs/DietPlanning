package com.example.a.dietplanning.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import com.example.a.dietplanning.activity.LoginActivity;
import com.example.a.dietplanning.utils.VariableUtil;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginService extends Service {
    public LoginService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle data = intent.getExtras();
        final String username = data.getString("user_id");
        final String password = data.getString("password");
       new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("user_id",username)
                        .add("password",password)
                        .build();
                Request request = new Request.Builder()
                        .url(VariableUtil.Service_IP +"login.do")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String str = response.body().string();
                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString("login",str);
                    System.out.print(str);
                    message.setData(data);
                    if(str.equals("密码错误")){
                        message.what = 0x001;
                        LoginActivity.handler.sendMessage(message);
                    }else if (str.equals("账户不存在")){
                        message.what = 0x002;
                        LoginActivity.handler.sendMessage(message);
                    }else {
                        message.what = 0x003;
                        LoginActivity.handler.sendMessage(message);
                    }
                    stopSelf();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return START_STICKY;
    }
}

