package com.example.freak.uiadaptivedemo.util;

import android.os.Build;
import android.text.TextUtils;

public class PhoneTrademarkUtil {
    private static PhoneTrademarkUtil mPhoneTrademarkUtil;
    public static PhoneTrademarkUtil getPhoneTrademark() {
        if (mPhoneTrademarkUtil==null){
            synchronized (PhoneTrademarkUtil.class){
                mPhoneTrademarkUtil = new PhoneTrademarkUtil();
            }
        }
        return mPhoneTrademarkUtil;
    }

    public final boolean isHuaWei() {
        String manufacturer = Build.MANUFACTURER;
        if (!TextUtils.isEmpty(manufacturer)){
            if (manufacturer.contains("HUAWEI")) return true;
        }
        return false;
    }

    public final boolean isMIUI() {
        String manufacturer = getSystemProperty("ro.miui.ui.version.name");
        if (!TextUtils.isEmpty(manufacturer)){
            return true;
        }
        return false;
    }

    public final boolean isOPPO() {
        String manufacturer = getSystemProperty("ro.product.brand");
        if (!TextUtils.isEmpty(manufacturer)){
            return true;
        }
        return false;
    }

    public final boolean isVIVO() {
        String manufacturer = this.getSystemProperty("ro.vivo.os.name");
        if (!TextUtils.isEmpty(manufacturer)){
            return true;
        }
        return false;
    }

    private  String getSystemProperty(String propName) {
        return SystemProperties.getInstance().get(propName);
    }
}
