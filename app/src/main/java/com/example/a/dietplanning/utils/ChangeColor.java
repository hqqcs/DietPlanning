package com.example.a.dietplanning.utils;

import android.app.Activity;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class ChangeColor {
    public static void changeColor(Activity activity, int color){
        //针对版本5.x以上的即LOLLIPOP以上的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //非全屏幕修改状态栏颜色
            Window window = activity.getWindow();
            //FLAG_TRANSLUCENT_STATUS为状态栏类型:半透明效果
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            //FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS为状态栏类型:支持着色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色  setStatusBarColor要求21以上
            window.setStatusBarColor(color);
            ViewGroup contentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            View childView = contentView.getChildAt(0);//对应activity布局文件根标签
            if (childView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                ViewCompat.setFitsSystemWindows(childView, true);
            }
        }
    }
}
