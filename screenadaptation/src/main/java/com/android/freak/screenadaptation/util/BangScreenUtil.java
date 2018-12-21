package com.android.freak.screenadaptation.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.freak.screenadaptation.util.trademark.AndroidPBangScreen;
import com.android.freak.screenadaptation.util.trademark.HuaWeiBangScreen;
import com.android.freak.screenadaptation.util.trademark.MIUIBangScreen;
import com.android.freak.screenadaptation.util.trademark.OPPOBangScreen;
import com.android.freak.screenadaptation.util.trademark.VIVOBangScreen;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class BangScreenUtil {
    private static BangScreenUtil mBangScreenUtil;
    protected static final String TAG = "BangScreenUtil";
    private final int systemCode = android.os.Build.VERSION.SDK_INT;
    private BangScreenSupport bangScreenSupport;
    private boolean isHaveResult;
    private boolean isBangScree;
    private int statusBarHeight = -1;
    public final static int DEVICE_HUAWEI = 0x0001;//华为
    public final static int DEVICE_OPPO = 0x0002;//OPPO
    public final static int DEVICE_VIVO = 0x0003;//VIVO
    public final static int DEVICE_MIUI = 0x0004;//小米
    /**
     * 获取刘海屏单例
     *
     * @return
     */
    public static BangScreenUtil getBangScreenInstance() {
        if (mBangScreenUtil == null) {
            synchronized (BangScreenUtil.class) {
                mBangScreenUtil = new BangScreenUtil();
            }
        }
        return mBangScreenUtil;
    }

    public BangScreenUtil() {
        bangScreenSupport = null;
    }

    /**
     * 判断是否是刘海屏
     *
     * @param Window
     * @return
     */
    public boolean hasBangScreen(Window Window) {
        if (!isHaveResult) {
            if (bangScreenSupport == null) checkScreenSupportInit();
            if (bangScreenSupport == null) {
                isHaveResult = true;
                return isBangScree = false;
            } else return isBangScree = bangScreenSupport.hasNotBangScreen(Window);
        } else return isBangScree;
    }

    /**
     * 获取刘海屏的大小
     */
    public List<Rect> getDisplayCutoutSize(Window window) {
        if (bangScreenSupport == null)
            checkScreenSupportInit();
        if (bangScreenSupport == null) return new ArrayList<Rect>();
        return bangScreenSupport.getBangSize(window);
    }

    private void checkScreenSupportInit() {
        if (bangScreenSupport == null) {
            if (systemCode < 26) {
                bangScreenSupport = new BangScreenSupport() {
                    @Override
                    public boolean hasNotBangScreen(Window window) {
                        return false;
                    }

                    @Override
                    public List<Rect> getBangSize(Window window) {
                        return new ArrayList<Rect>();
                    }

                    @Override
                    public void extendStatusCutout(Window window, Context context) {

                    }

                    @Override
                    public void setWindowLayoutBlockNotch(Window window) {

                    }

                    @Override
                    public void transparentStatusCutout(Window window, Context context) {

                    }

                    @Override
                    public void fullscreen(Window window, Context context) {

                    }
                };
            } else if (systemCode < 28) {
                PhoneTrademarkUtil phoneTrademarkUtil = PhoneTrademarkUtil.getPhoneTrademark();
                if (phoneTrademarkUtil.isHuaWei()) {
                    Log.i(TAG, "HuaWei");
                    bangScreenSupport = new HuaWeiBangScreen();
                } else if (phoneTrademarkUtil.isMIUI()) {
                    Log.i(TAG, "MIUI");
                    bangScreenSupport = new MIUIBangScreen();
                } else if (phoneTrademarkUtil.isVIVO()) {
                    Log.i(TAG, "VIVO");
                    bangScreenSupport = new VIVOBangScreen();
                } else if (phoneTrademarkUtil.isOPPO()) {
                    Log.i(TAG, "OPPO");
                    bangScreenSupport = new OPPOBangScreen();
                }
            } else {
                Log.i(TAG, "Android P");
                bangScreenSupport = new AndroidPBangScreen();
            }
        }
    }


    /**
     * 获取状态栏高度
     *
     * @param context
     * @return 状态栏高度
     */
    public final int getStatusBarHeight(Context context) {
        if (statusBarHeight != -1) return statusBarHeight;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resId);
        }
        return statusBarHeight;
    }


    /**
     * 全面屏.
     *
     * @param window  the window
     * @param context the context
     */
    public void fullscreen(Window window, Context context) {
        if (bangScreenSupport == null)
            checkScreenSupportInit();
        if (window == null) return;
        if (bangScreenSupport == null) return;
        if (context instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
            Log.i(TAG, "isAppCompatActivity");
            if (actionBar != null) actionBar.hide();

        } else if (context instanceof Activity) {
            android.app.ActionBar actionBar = ((Activity) context).getActionBar();
            if (actionBar != null) actionBar.hide();
        }
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
        bangScreenSupport.fullscreen(window, context);
    }

    /**
     * 设置始终不使用凹口屏区域
     *
     * @param window the window
     */
    public void blockDisplayCutout(Window window) {
        if (bangScreenSupport == null)
            checkScreenSupportInit();
        if (bangScreenSupport != null) bangScreenSupport.setWindowLayoutBlockNotch(window);
    }

    /**
     * 设置始终使用凹口屏区域并将状态栏隐藏  隐藏状态栏
     * always to use bangScreen and layout extend to status bar,
     * and status bar was hide
     *
     * @param window  the window
     * @param context the context
     */
    public void extendStatusCutout(Window window, Context context) {
        if (bangScreenSupport == null)
            checkScreenSupportInit();
        if (window == null) return;
        if (bangScreenSupport == null) return;

        if (context instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
            Log.i(TAG, "isAppCompatActivity");
            if (actionBar != null) actionBar.hide();

        } else if (context instanceof Activity) {
            android.app.ActionBar actionBar = ((Activity) context).getActionBar();
            if (actionBar != null) actionBar.hide();
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | SYSTEM_UI_FLAG_LAYOUT_STABLE);
        bangScreenSupport.extendStatusCutout(window, context);
    }

    /**
     * 设置始终使用凹口屏区域并将状态栏透明 沉浸式状态栏
     * * always to use bangScreen and layout extend to status bar,
     * and status bar was transparent
     *
     * @param window  the window
     * @param context the context
     */
    public void transparentStatusCutout(Window window, Context context) {
        if (bangScreenSupport == null)
            checkScreenSupportInit();
        if (window == null) return;
        if (bangScreenSupport == null) return;

        if (context instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
            Log.i(TAG, "isAppCompatActivity");
            if (actionBar != null) actionBar.hide();

        } else if (context instanceof Activity) {
            android.app.ActionBar actionBar = ((Activity) context).getActionBar();
            if (actionBar != null) actionBar.hide();

        }
    }


    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static int getDeviceName() {
        PhoneTrademarkUtil phoneTrademarkUtil = PhoneTrademarkUtil.getPhoneTrademark();
        if (phoneTrademarkUtil.isHuaWei()) {
            Log.i(TAG, "HuaWei");
            return DEVICE_HUAWEI;
        } else if (phoneTrademarkUtil.isMIUI()) {
            Log.i(TAG, "MIUI");
            return DEVICE_MIUI;
        } else if (phoneTrademarkUtil.isVIVO()) {
            Log.i(TAG, "VIVO");
            return DEVICE_VIVO;
        } else if (phoneTrademarkUtil.isOPPO()) {
            Log.i(TAG, "OPPO");
            return DEVICE_OPPO;
        }
        return 0;
    }

}
