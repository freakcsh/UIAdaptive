package com.example.freak.uiadaptivedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.example.freak.uiadaptivedemo.base.IActivityStatusBar;
import com.example.freak.uiadaptivedemo.util.BangScreenUtil;

public class AdaptiveActivity extends AppCompatActivity implements IActivityStatusBar {

    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_adaptive);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getStatusBarColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    public void initData() {
        String type = getIntent().getStringExtra("type");
        switch (type) {
            case "0":
                BangScreenUtil.getBangScreenInstance().transparentStatusCutout(window, this);
                break;
            case "1":
                BangScreenUtil.getBangScreenInstance().extendStatusCutout(window, this);

                break;
            case "2":
                BangScreenUtil.getBangScreenInstance().fullscreen(window, this);
                break;
            case "3":
                BangScreenUtil.getBangScreenInstance().blockDisplayCutout(window);
                break;
            default:
                break;
        }
    }

    @Override
    public void initView() {
        window = getWindow();
        Log.e("TAG","刘海屏高度"+ BangScreenUtil.getBangScreenInstance().getDisplayCutoutSize(window));
    }
}
