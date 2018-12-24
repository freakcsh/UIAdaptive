package com.example.freak.uiadaptive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.freak.screenadaptation.util.BangScreenUtil;
import com.example.freak.uiadaptive.base.IActivityStatusBar;
import com.orhanobut.logger.Logger;

public class MainActivity extends AppCompatActivity implements IActivityStatusBar {
    private TextView mTextViewDeviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getStatusBarColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    public void initData() {
        Logger.e("测试创建分支");
    }

    @Override
    public void initView() {
        mTextViewDeviceName = findViewById(R.id.text_view_device_name);
        Logger.e("创建分支后修改");
    }

    /**
     * 沉浸式状态栏
     *
     * @param view
     */
    public void immersion(View view) {
        startActivity(new Intent(this, AdaptiveActivity.class).putExtra("type", "0"));
    }

    /**
     * 隐藏状态栏
     *
     * @param view
     */
    public void gone(View view) {
        startActivity(new Intent(this, AdaptiveActivity.class).putExtra("type", "1"));
    }

    /**
     * 全面屏
     *
     * @param view
     */
    public void overall(View view) {
        startActivity(new Intent(this, AdaptiveActivity.class).putExtra("type", "2"));
    }

    /**
     * 不使用刘海屏
     *
     * @param view
     */
    public void unUseBang(View view) {
        startActivity(new Intent(this, AdaptiveActivity.class).putExtra("type", "3"));
    }

    /**
     * 检测手机品牌
     *
     * @param view
     */
    public void detection(View view) {
        if (BangScreenUtil.getDeviceName() == BangScreenUtil.DEVICE_HUAWEI) {
            mTextViewDeviceName.setText("华为");
        } else if (BangScreenUtil.getDeviceName() == BangScreenUtil.DEVICE_MIUI) {
            mTextViewDeviceName.setText("小米");
        } else if (BangScreenUtil.getDeviceName() == BangScreenUtil.DEVICE_OPPO) {
            mTextViewDeviceName.setText("OPPO");
        } else if (BangScreenUtil.getDeviceName() == BangScreenUtil.DEVICE_VIVO) {
            mTextViewDeviceName.setText("VIVO");
        }
    }
}
