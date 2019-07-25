package com.example.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UsageTest";
    UsageStatsManager mUsageStatsManager;
    TextView content;
    SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.get_usage).setOnClickListener(this);
        findViewById(R.id.get_shortcuts).setOnClickListener(this);
        findViewById(R.id.get_shortcuts).setOnClickListener(this);
        findViewById(R.id.get_intersect).setOnClickListener(this);
        content = findViewById(R.id.content);
        content.setMovementMethod(ScrollingMovementMethod.getInstance());
        mUsageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private List<Pair<Integer, String>> getUsageData() {
        HashMap<String, Integer> counterMap = new HashMap<>();
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 14L * 24 * 60 * 60 * 1000;
//        Map<String, UsageStats> queryResult = mUsageStatsManager.queryAndAggregateUsageStats(beginTime, endTime);

        UsageEvents usageEvents = mUsageStatsManager.queryEvents(beginTime, endTime);
        UsageEvents.Event usageEvent = new UsageEvents.Event();
        StringBuilder builder = new StringBuilder();
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(usageEvent);
            int eventType = usageEvent.getEventType();
            switch (eventType) {
                case UsageEvents.Event.MOVE_TO_BACKGROUND:
                    builder.append(format.format(new Date(usageEvent.getTimeStamp()))).append(" -> ").append("离开了：");
                    break;
                case UsageEvents.Event.MOVE_TO_FOREGROUND:
                    builder.append(format.format(new Date(usageEvent.getTimeStamp()))).append(" -> ").append("来到了：");
                    break;
                default:
                    continue;
            }
            builder.append(usageEvent.getPackageName()).append(",").append(usageEvent.getClassName());
            builder.append("\n");
            String key = usageEvent.getPackageName() + ":" + usageEvent.getClassName();
            Integer v = counterMap.get(key);
            if (v == null) {
                v = 1;
            } else {
                v = v + 1;
            }
            counterMap.put(key, v);
        }
        ArrayList<Pair<Integer, String>> pairs = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : counterMap.entrySet()) {
            pairs.add(new Pair<Integer, String>(entry.getValue(), entry.getKey()));
        }
        pairs.sort(new Comparator<Pair<Integer, String>>() {
            @Override
            public int compare(Pair<Integer, String> o1, Pair<Integer, String> o2) {
                return -o1.first + o2.first;
            }
        });
        return pairs;
    }

    public static boolean checkAppUsagePermission(Context context) {
        boolean granted = false;
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mode == AppOpsManager.MODE_DEFAULT) {
            granted = (context.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        return granted;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openPermissionActivity() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), 1003);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_usage:
                StringBuilder builder = new StringBuilder();
                if (!checkAppUsagePermission(this)) {
                    openPermissionActivity();
                } else {
                    List<Pair<Integer, String>> pairs = getUsageData();
                    for (Pair<Integer, String> pair : pairs) {
                        builder.append(pair.second).append(",").append(pair.first).append("\n");
                    }
                    content.setText(builder.toString());
                }
                break;
            case R.id.get_shortcuts:
                builder = new StringBuilder();
                HashMap<String, List<ShortcutInfo>> infoForAllApps = getShortcutInfoForAllApps();
                for (Map.Entry<String, List<ShortcutInfo>> entry : infoForAllApps.entrySet()) {
                    String key = entry.getKey();
                    builder.append(key).append("\n");
                    List<ShortcutInfo> shortcutInfos = entry.getValue();
                    for (ShortcutInfo shortcutInfo : shortcutInfos) {
                        ComponentName activity = shortcutInfo.getActivity();
                        if (activity != null) {
                            Drawable icon = getIcon(this, shortcutInfo);
                            builder.append("\t").append(shortcutInfo.getShortLabel()).append(": ").append(activity.getClassName()).append("\n");
                        } else {
                            builder.append("\t").append(shortcutInfo.getShortLabel()).append(": ").append("no activity").append("\n");
                        }
                    }
                    builder.append("\n");
                }
                content.setText(builder.toString());
                break;
            case R.id.get_intersect:
                if (!checkAppUsagePermission(this)) {
                    openPermissionActivity();
                } else {
                    List<Pair<Integer, String>> usageData = getUsageData();
                    builder = new StringBuilder();
                    for (Pair<Integer, String> usageDatum : usageData) {
                        String second = usageDatum.second;
                        String[] split = second.split(":");
                        String packageName = split[0].trim();
                        String actName = split[1].trim();
                        List<ShortcutInfo> shortcutInfo = getShortcutInfo(packageName, 10);
                        if (shortcutInfo.size() > 0) {
                            for (ShortcutInfo info : shortcutInfo) {
                                ComponentName activity = info.getActivity();
                                if (activity != null) {
                                    String className = activity.getClassName();
                                    if (className.equals(actName)) {
                                        builder.append(packageName).append("\n");
                                        Drawable icon = getIcon(this, info);
                                        builder.append("\t").append(info.getShortLabel()).append(", 次数：" + usageDatum.first).append(": ").append(className).append("\n");
                                    }
                                }
                            }
                        }
                    }
                    content.setText(builder.toString());
                }
                break;
        }
    }

    private HashMap<String, List<ShortcutInfo>> getShortcutInfoForAllApps() {
        HashMap<String, List<ShortcutInfo>> stringListHashMap = new HashMap<>();
        ArrayList<PackageInfo> installApps = (ArrayList<PackageInfo>) getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < installApps.size(); i++) {
            try {
                PackageInfo packageInfo = installApps.get(i);
                packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
//                packageInfo.versionName;
//                packageInfo.versionCode;
                List<ShortcutInfo> shortcutInfo = getShortcutInfo(packageInfo.packageName, 20);
                if (shortcutInfo.size() > 0) {
                    stringListHashMap.put(packageInfo.packageName, shortcutInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stringListHashMap;
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    List<ShortcutInfo> getShortcutInfo(String packageName, int maxAppShortcut) {
        List<android.content.pm.ShortcutInfo> result = new ArrayList<>();
        if (TextUtils.isEmpty(packageName)) {
            return result;
        }
        LauncherApps launcherApps = (LauncherApps) getSystemService(Context.LAUNCHER_APPS_SERVICE);
        LauncherApps.ShortcutQuery shortcutQuery = new LauncherApps.ShortcutQuery();
        shortcutQuery.setPackage(packageName);
        shortcutQuery.setQueryFlags(LauncherApps.ShortcutQuery.FLAG_MATCH_MANIFEST |
                LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC |
                LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED
        );
        try {
            List<android.content.pm.ShortcutInfo> infoList = launcherApps.getShortcuts(shortcutQuery, android.os.Process.myUserHandle());
            if (infoList == null)
                return result;
            for (final android.content.pm.ShortcutInfo info : infoList) {
                if (result.size() >= maxAppShortcut)
                    break;
                result.add(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Drawable getIcon(Context context, ShortcutInfo info) {
        int density = context.getResources().getDisplayMetrics().densityDpi;
        if (density <= 0) {
            density = DisplayMetrics.DENSITY_DEFAULT;
        }
        Drawable drawable = null;
        try {
            LauncherApps mLauncherApps = (LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
            drawable = mLauncherApps.getShortcutBadgedIconDrawable(info, density);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }
}
