package com.jacky.launcher.auto;

import android.content.Context;
import android.content.SharedPreferences;

public class AutoLaunchTool {

    private static final String AUTO_LAUNCH = "auto_launch";
    private static final String KEY_PACKAGE_NAME = "key_package_name";

    private static final String KEY_AUTO_DELAY = "key_auto_delay";

    public static void saveAutoLaunchPackage(Context context, String packageName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AUTO_LAUNCH, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(KEY_PACKAGE_NAME, packageName);
        edit.apply();
    }

    public static String getAutoLaunchPackageName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AUTO_LAUNCH, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PACKAGE_NAME, null);
    }

    public static void saveAutoLaunchDelaySeconds(Context context, int delaySeconds) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AUTO_LAUNCH, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(KEY_AUTO_DELAY, delaySeconds);
        edit.apply();
    }

    public static int getAutoLaunchDelay(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AUTO_LAUNCH, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_AUTO_DELAY, 0);
    }

}
