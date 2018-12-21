package com.example.freak.uiadaptivedemo.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.android.freak.screenadaptation.util.BangScreenUtil;
import com.example.freak.uiadaptivedemo.R;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class App  extends MultiDexApplication {
    private static final int DESIGN_WIDTH = 375;
    private static App mApp;
    private static Context mContext;

    public static App getInstance() {
        if (mApp == null) {
            synchronized (App.class) {
                if (mApp == null) {
                    mApp = new App();
                }
            }
        }
        return mApp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        final Context context = this;
        FormatStrategy mFormatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // （可选）是否显示线程信息。默认值true
//                .methodCount(5)         // （可选）要显示的方法行数。默认值2
//                .methodOffset(7)        // （可选）隐藏内部方法调用到偏移量。默认值5
//                .logStrategy() // （可选）更改要打印的日志策略。默认LogCat
                .tag("UIAdaptive")   // （可选）每个日志的全局标记。默认PRETTY_LOGGER .build
                .build();
        //log日志打印框架Logger
        Logger.addLogAdapter(new AndroidLogAdapter(mFormatStrategy));
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //限制竖屏
                Log.e("TAG","状态栏高度"+getStatusBarHeight());
//                a(activity);
                resetDensity(context, DESIGN_WIDTH);
                resetDensity(activity, DESIGN_WIDTH);
                setImmersiveStatusBar(activity);
                Log.e("TAG","状态栏高度"+getStatusBarHeight());
                Logger.e("大小"+getNotchSize(context).length);
                Logger.d(getNotchSize(context));


                if (activity instanceof IActivityBase) {
                    ((IActivityBase) activity).initView();
                    ((IActivityBase) activity).initData();
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                setToolBar(activity);
                resetDensity(context, DESIGN_WIDTH);
                resetDensity(activity, DESIGN_WIDTH);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                resetDensity(context, DESIGN_WIDTH);
                resetDensity(activity, DESIGN_WIDTH);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * 以pt为单位重新计算大小
     */
    public static void resetDensity(Context context, float designWidth) {
        if (context == null)
            return;
        Point size = new Point();
        ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(size);
        Resources resources = context.getResources();
        resources.getDisplayMetrics().xdpi = size.x / designWidth * 72f;
        DisplayMetrics metrics = getMetricsOnMIUI(context.getResources());
        if (metrics != null)
            metrics.xdpi = size.x / designWidth * 72f;
    }

    /**
     * 解决MIUI屏幕适配问题
     *
     * @param resources
     * @return
     */
    private static DisplayMetrics getMetricsOnMIUI(Resources resources) {
        if ("MiuiResources".equals(resources.getClass().getSimpleName()) || "XResources".equals(resources.getClass().getSimpleName())) {
            try {
                Field field = Resources.class.getDeclaredField("mTmpMetrics");
                field.setAccessible(true);
                return (DisplayMetrics) field.get(resources);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public void a(Activity activity){
        Window window=activity.getWindow();
        BangScreenUtil.getBangScreenInstance().blockDisplayCutout(window);
    }

    /**
     * 设置状态栏
     *
     * @param activity
     */
    private void setImmersiveStatusBar(Activity activity) {
        if (activity instanceof IActivityStatusBar) {
            if (((IActivityStatusBar) activity).getStatusBarColor() != 0) {
                setTranslucentStatus(activity);
                addImmersiveStatusBar(activity, ((IActivityStatusBar) activity).getStatusBarColor());
            }
        }
    }

    /**
     * 设置状态栏为透明
     *
     * @param activity
     */
    private void setTranslucentStatus(Activity activity) {
        //******** 5.0以上系统状态栏透明 ********
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 添加自定义状态栏
     *
     * @param activity
     */
    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    private void addImmersiveStatusBar(Activity activity, int color) {
        ViewGroup contentFrameLayout = activity.findViewById(android.R.id.content);
        View contentView = contentFrameLayout.getChildAt(0);
        if (contentView != null && Build.VERSION.SDK_INT >= 14) {
            contentView.setFitsSystemWindows(true);
        }

        View statusBar = new View(activity);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Window window=activity.getWindow();
        /**
         * 使用PT适配之后刘海屏的高度会改变，如果是刘海屏，则获取刘海屏的状态栏高度，如果不是刘海屏，则获取系统的状态栏高度即可
         */
        if (BangScreenUtil.getBangScreenInstance().hasBangScreen(window)){
            Logger.e("是刘海屏");
            params.height=getNotchSize(activity)[1];
        }else {
            Logger.e("不是刘海屏");
            params.height = getStatusBarHeight();
        }
//        params.height = getStatusBarHeight();
        statusBar.setLayoutParams(params);
        statusBar.setBackgroundColor(color);
        contentFrameLayout.addView(statusBar);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 设置ToolBar
     *
     * @param activity
     */
    private void setToolBar(final Activity activity) {
        if (activity.findViewById(R.id.tool_bar) != null && ((AppCompatActivity) activity).getSupportActionBar() == null) {
            Toolbar toolbar = activity.findViewById(R.id.tool_bar);
            if (!TextUtils.isEmpty(activity.getTitle())) {
                toolbar.setTitle(activity.getTitle());
            } else {
                toolbar.setTitle("");
            }

            if (((IActivityStatusBar) activity).getStatusBarColor() != 0) {
                toolbar.setBackgroundColor(((IActivityStatusBar) activity).getStatusBarColor());
            } else {
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }

            ((AppCompatActivity) activity).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            });
        }
    }

    //获取华为刘海的高宽
    public static int[] getNotchSize(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("haha", "getNotchSize ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("haha", "getNotchSize NoSuchMethodException");
        } catch (Exception e) {
            Log.e("haha", "getNotchSize Exception");
        } finally {
            return ret;
        }
    }
}
