package com.jacky.launcher.auto;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jacky.launcher.R;
import com.jacky.launcher.adapter.AppUninstallAdapter;
import com.jacky.launcher.app.AppDataManage;
import com.jacky.launcher.app.AppModel;
import com.jacky.launcher.util.Tools;

import java.util.List;

/**
 * 应用卸载类
 *
 * @author jacky
 * @version 1.0
 * @since 2016.4.1
 */
public class AppAutoLaunchSettingActivity extends Activity implements View.OnClickListener {

    private ListView listView;
    private AppAutoLaunchAdapter adapter;
    private List<AppModel> mAppList;
    private Context context;
    private Receiver receiver;
    private TextView tvDelaySeconds;
    private TextView tvAutoLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.app_auto_launch);
        context = this;
        init();
    }

    private void init() {

        tvAutoLaunch = findViewById(R.id.tv_auto_launch);
        Button btnRemoveAutoLaunch = findViewById(R.id.btn_remove_auto_launch);
        btnRemoveAutoLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoLaunchTool.saveAutoLaunchPackage(AppAutoLaunchSettingActivity.this, null);
                updateAutoLaunchInfo();
            }
        });

        Button btnMinus = findViewById(R.id.btn_minus);
        tvDelaySeconds = findViewById(R.id.tv_delay_second);
        Button btnAdd = findViewById(R.id.btn_add);

        updateAutoLaunchInfo();

        updateDelayText();

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int autoLaunchDelay = AutoLaunchTool.getAutoLaunchDelay(AppAutoLaunchSettingActivity.this);
                if (autoLaunchDelay > 0) {
                    --autoLaunchDelay;
                    AutoLaunchTool.saveAutoLaunchDelaySeconds(AppAutoLaunchSettingActivity.this, autoLaunchDelay);
                    updateDelayText();
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int autoLaunchDelay = AutoLaunchTool.getAutoLaunchDelay(AppAutoLaunchSettingActivity.this);
                ++autoLaunchDelay;
                AutoLaunchTool.saveAutoLaunchDelaySeconds(AppAutoLaunchSettingActivity.this, autoLaunchDelay);
                updateDelayText();
            }
        });

        listView = (ListView) findViewById(R.id.app_auto_launch_lv);
        AppDataManage getAppInstance = new AppDataManage(context);
        mAppList = getAppInstance.getUninstallAppList();
        adapter = new AppAutoLaunchAdapter(context, mAppList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AutoLaunchTool.saveAutoLaunchPackage(AppAutoLaunchSettingActivity.this, mAppList.get(position).getPackageName());
                updateAutoLaunchInfo();
            }
        });
    }

    private void updateAutoLaunchInfo() {
        String autoLaunchPackageName = AutoLaunchTool.getAutoLaunchPackageName(this);
        if (autoLaunchPackageName != null && !autoLaunchPackageName.isEmpty()) {
            String appName = Tools.getAppName(this, autoLaunchPackageName);
            tvAutoLaunch.setText(appName);
            Drawable appIconByPackageName = Tools.getAppIconByPackageName(this, autoLaunchPackageName);
            float px = Tools.convertDpToPx(this, 56);
            appIconByPackageName.setBounds(0, 0, (int) px, (int) px);
            tvAutoLaunch.setCompoundDrawables(appIconByPackageName, null, null, null);
        } else {
            tvAutoLaunch.setText("");
            tvAutoLaunch.setCompoundDrawables(null, null, null, null);
        }
    }

    private void updateDelayText() {
        tvDelaySeconds.setText(String.valueOf(AutoLaunchTool.getAutoLaunchDelay(this)));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addDataScheme("package");
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //接收安装广播
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {

                String packageName = intent.getDataString();
                List<ResolveInfo> list = Tools.findActivitiesForPackage(context, packageName);
                ResolveInfo info = list.get(0);
                PackageManager localPackageManager = context.getPackageManager();
                AppModel localAppBean = new AppModel();
                localAppBean.setIcon(info.activityInfo.loadIcon(localPackageManager));
                localAppBean.setName(info.activityInfo.loadLabel(localPackageManager).toString());
                localAppBean.setPackageName(info.activityInfo.packageName);
                localAppBean.setDataDir(info.activityInfo.applicationInfo.publicSourceDir);

                mAppList.add(localAppBean);
            }
            //接收卸载广播
            if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
                String receiverName = intent.getDataString();
                receiverName = receiverName.substring(8);
                AppModel appBean;
                for (int i = 0; i < mAppList.size(); i++) {
                    appBean = mAppList.get(i);
                    String packageName = appBean.getPackageName();
                    if (packageName.equals(receiverName)) {
                        mAppList.remove(i);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
}