package com.example.a.dietplanning.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a.dietplanning.R;
import com.example.a.dietplanning.db.User;
import com.example.a.dietplanning.utils.ChangeColor;
import com.example.a.dietplanning.utils.ToastUtil;
import com.google.gson.Gson;

import java.text.DecimalFormat;

public class BodyDataActivity extends BaseActivity implements View.OnClickListener {
    private TextView weightTv,heightTv,BIMTv,ageTv,sexTv,saveTv;
    private LinearLayout weightLL,heightLL,BIMLL,ageLL,sexLL;
    private double weight,height,BIM;
    private User user;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_data);
        ChangeColor.changeColor(this,Color.parseColor("#584f60"));
        initComponent();
        pref=PreferenceManager.getDefaultSharedPreferences(BodyDataActivity.this);
        editor=pref.edit();
        String userStr=pref.getString("user_data",null);
        user=new Gson().fromJson(userStr,User.class);
        weight=user.getWeight();
        height=user.getHeight();
        BIM=(weight/(height*height))*10000;
        setData();
        weightLL.setOnClickListener(this);
        heightLL.setOnClickListener(this);
        BIMLL.setOnClickListener(this);
        ageLL.setOnClickListener(this);
        sexLL.setOnClickListener(this);
        saveTv.setOnClickListener(this);
    }
    /**
     * 初始化各组件
     */
    private void initComponent() {
        weightTv=(TextView)findViewById(R.id.weight);
        heightTv=(TextView)findViewById(R.id.height);
        BIMTv=(TextView)findViewById(R.id.bim);
        ageTv=(TextView)findViewById(R.id.age);
        sexTv=(TextView)findViewById(R.id.sex);
        saveTv=(TextView)findViewById(R.id.save);
        weightLL=(LinearLayout)findViewById(R.id.weight_ll);
        heightLL=(LinearLayout)findViewById(R.id.height_ll);
        BIMLL=(LinearLayout)findViewById(R.id.bim_ll);
        ageLL=(LinearLayout)findViewById(R.id.age_ll);
        sexLL=(LinearLayout)findViewById(R.id.sex_ll);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weight_ll:
                dialogEditText("请输入当前体重", "kg", weightTv);
                break;
            case R.id.height_ll:
                dialogEditText("请输入当前身高", "cm", heightTv);
                break;
            case R.id.save:
                user.setHeight(Double.parseDouble(heightTv.getText().toString().replaceAll("[^0-9.]", "")));
                user.setWeight(Double.parseDouble(weightTv.getText().toString().replaceAll("[^0-9.]", "")));
                editor.putString("user_data",new Gson().toJson(user));
                editor.apply();
                break;
        }
    }
    /**
     *为各组件设置数据
     */
    private void setData(){
        DecimalFormat df=new DecimalFormat("0.0");
        weightTv.setText(weight+"kg");
        heightTv.setText(height+"cm");
        BIMTv.setText(df.format(BIM));
    }
    /**
     * 可输入的对框框
     */
    private void dialogEditText(String info, final String unit, final TextView textView) {
        final AlertDialog grDdialog = new AlertDialog.Builder(this).create();
        LayoutInflater flater = LayoutInflater.from(this);
        final View view = flater.inflate(R.layout.edit_dialog, null);
        Button okBtn=(Button)view.findViewById(R.id.btn_ok);
        TextView cancel=(TextView) view.findViewById(R.id.cancel);
        final EditText editText=(EditText)view.findViewById(R.id.user_info) ;
        TextView unitTv=(TextView)view.findViewById(R.id.unit);
        TextView infoTv=(TextView)view.findViewById(R.id.info);
        unitTv.setText(unit);
        infoTv.setText(info);
        grDdialog.setView(view);
        grDdialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                // 解决EditText, 在dialog中无法自动弹出对话框的问题
                showKeyboard(editText);
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editText.getText().toString())){
                    textView.setText(editText.getText().toString()+unit);
                    grDdialog.dismiss();
                } else {
                    ToastUtil.showToast(BodyDataActivity.this, "请确保输入的内容不为空!");
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grDdialog.dismiss();
            }
        });
        grDdialog.show();

    }
    /**
     * 解决在dialog中无法自动弹出对话框的问题
     **/
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void showKeyboard(EditText editText) {
        if (editText != null) {
            //设置可获得焦点
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            //请求获得焦点
            editText.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
