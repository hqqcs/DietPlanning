package com.example.a.dietplanning.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.a.dietplanning.activity.SearchFoodActivity;
import com.example.a.dietplanning.utils.VariableUtil;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchFoodService extends Service {
    public SearchFoodService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String foodName=intent.getStringExtra("food_name");
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("food_name",foodName)
                        .build();
                Request request = new Request.Builder()
                        .url(VariableUtil.Service_IP +"food/MinuteFood.do")
                        .post(requestBody)
                        .build();
                String str="网络连接失败，请重试";//初始化服务器返回的数据
                try {
                    Message message = new Message();
                    Bundle data = new Bundle();
                    Response response = client.newCall(request).execute();
                    str = response.body().string();
                    data.putString("food_detail",str);
                    Log.v("1","发送"+str);
                    message.setData(data);
                    message.what = 0x009;
                    SearchFoodActivity.mHandler.sendMessage(message);
                    stopSelf();//关闭服务
                } catch (IOException e) {
                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString("food_detail",str);
                    message.setData(data);
                    message.what = 0x010;
                    SearchFoodActivity.mHandler.sendMessage(message);
                }
            }
        }).start();
        return START_STICKY;
    }
}
