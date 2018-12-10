package com.example.freak.uiadaptivedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.freak.uiadaptivedemo.base.IActivityStatusBar;

public class MainActivity extends AppCompatActivity implements IActivityStatusBar {

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

    }

    @Override
    public void initView() {

    }

    /**
     * 沉浸式状态栏
     * @param view
     */
    public void immersion(View view) {
        startActivity(new Intent(this,AdaptiveActivity.class).putExtra("type","0"));
    }

    /**
     * 隐藏状态栏
     * @param view
     */
    public void gone(View view) {
        startActivity(new Intent(this,AdaptiveActivity.class).putExtra("type","1"));
    }

    /**
     * 全面屏
     * @param view
     */
    public void overall(View view) {
        startActivity(new Intent(this,AdaptiveActivity.class).putExtra("type","2"));
    }

    /**
     * 不使用刘海屏
     * @param view
     */
      public void unUseBang(View view) {
        startActivity(new Intent(this,AdaptiveActivity.class).putExtra("type","3"));
    }
}
