package com.ych.unit;

import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

public class StringUtil {
    /**
     * 判断字符串是否为null或空
     *
     * @param string
     * @return true:为 空或null;false:不为 空或null
     */
    public static boolean isNullOrEmpty(String string) {
        boolean flag = false;
        if (null == string || string.trim().length() == 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return
     */
    public static int getWidth(AppCompatActivity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        return width;
    }

}
