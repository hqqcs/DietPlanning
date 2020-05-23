package com.example.a.dietplanning.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;

import com.example.a.dietplanning.R;

public class DensityUtil {

    public static int dp2px(Context context, float dpValue) {
        return (int)(dpValue * getDensity(context) + 0.5F);
    }

    private static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }


    public static void setIcon(SearchView seach, String hint, String text) {
        SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete) seach.findViewById(R.id.search_src_text);
        textView.setTextColor(Color.WHITE);
        if (TextUtils.isEmpty(text)) {
            textView.setHint(hint);
        } else {
            textView.setText(text);
        }
        textView.setTextSize(14);
        textView.setHintTextColor(Color.WHITE);
        AppCompatImageView button2 = (AppCompatImageView) seach.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        button2.setImageResource(R.drawable.cha);
        AppCompatImageView magImage = (AppCompatImageView) seach.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        magImage.setImageResource(R.drawable.search_icon);
    }
}
