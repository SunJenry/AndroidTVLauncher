
package com.jacky.launcher.function;

import android.content.Context;
import android.content.Intent;

import com.jacky.launcher.R;
import com.jacky.launcher.app.AppUninstall;
import com.jacky.launcher.auto.AppAutoLaunchSettingActivity;

import java.util.ArrayList;
import java.util.List;

public class FunctionModel {

    private int icon;
    private String id;
    private String name;
    private Intent mIntent;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public void setIntent(Intent intent) {
        mIntent = intent;
    }

    public static List<FunctionModel> getFunctionList(Context context) {
        List<FunctionModel> functionModels = new ArrayList<>();

        FunctionModel appUninstall = new FunctionModel();
        appUninstall.setName("应用卸载");
        appUninstall.setIcon(R.drawable.ic_app_uninstall);
        appUninstall.setIntent(new Intent(context, AppUninstall.class));
        functionModels.add(appUninstall);

        FunctionModel appAutoLaunch = new FunctionModel();
        appAutoLaunch.setName("自启动");
        appAutoLaunch.setIcon(R.drawable.pic_default);
        appAutoLaunch.setIntent(new Intent(context, AppAutoLaunchSettingActivity.class));
        functionModels.add(appAutoLaunch);

        return functionModels;
    }
}
