package com.position.wyh.position.utlis;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.position.wyh.position.AccessibilityServiceBase;

import java.util.List;

public class SystemUtil {

    public static boolean getProcessList(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {

            LogUtils.e("====", "====" + appProcess.processName);
//            if (appProcess.processName.equals(context.getPackageName())) {
//                /*
//                BACKGROUND=400 EMPTY=500 FOREGROUND=100
//                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
//                 */
//                Log.i(context.getPackageName(), "此appimportace ="
//                        + appProcess.importance
//                        + ",context.getClass().getName()="
//                        + context.getClass().getName());
//                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    Log.i(context.getPackageName(), "处于后台"
//                            + appProcess.processName);
//                    return true;
//                } else {
//                    Log.i(context.getPackageName(), "处于前台"
//                            + appProcess.processName);
//                    return false;
//                }
//            }
        }
        return false;
    }

    /**
     * 判断进程是否存活
     */
    public static boolean isProcessExist(Context context, int pid) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lists;
        if (am != null) {
            lists = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo appProcess : lists) {
                if (appProcess.pid == pid) {
                    Log.e("TAG", "333333");
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 判断应用是否在运行
     *
     * @param context
     * @return
     */
    public static boolean isRun(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        String MY_PKG_NAME = "cmb.pb";
        //100表示取的最大的任务数，info.topActivity表示当前正在运行的Activity，info.baseActivity表系统后台有此进程在运行
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isAppRunning = true;
                Log.i("ActivityService isRun()", info.topActivity.getPackageName() + " info.baseActivity.getPackageName()=" + info.baseActivity.getPackageName());
                break;
            }
        }
        Log.i("ActivityService isRun()", "======com.ad 程序  ...isAppRunning......" + isAppRunning);
        return isAppRunning;
    }

    public static String getTopApp(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager m = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            if (m != null) {
                long now = System.currentTimeMillis();
                //获取60秒之内的应用数据
                List<UsageStats> stats = m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, now - 60 * 1000, now);
                Log.i("1111", "Running app number in last 60 seconds : " + stats.size());

                String topActivity = "";

                //取得最近运行的一个app，即当前运行的app
                if ((stats != null) && (!stats.isEmpty())) {
                    int j = 0;
                    for (int i = 0; i < stats.size(); i++) {
                        if (stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed()) {
                            j = i;
                        }
                    }
                    topActivity = stats.get(j).getPackageName();
                }
                Log.i("1111", "======top running app is : " + topActivity);
                return topActivity;
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public static void getTopAppProcess(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager m = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            if (m != null) {
                long now = System.currentTimeMillis();
                //获取2秒之内的应用数据
                List<UsageStats> stats = m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, now - 2 * 1000, now);
                Log.i("1111", "Running app number in last 60 seconds : " + stats.size());
                if (stats != null && stats.size() > 0) {
                    for (UsageStats usageStats : stats) {
                        String packageName = usageStats.getPackageName();
                        LogUtils.e("======", "======package:" + packageName);
                    }
                }
            }
        }
    }

    /**
     * 检测一个android程序是否在运行
     *
     * @param context
     * @param PackageName
     * @return
     */
    public static boolean isServiceStarted(Context context, String PackageName) {
        //<uses-permission android:name="android.permission.GET_TASKS"/>
        //https://crazier9527.iteye.com/blog/1476134
        boolean isStarted = false;
        try {
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            int intGetTastCounter = 1000;
            List<ActivityManager.RunningServiceInfo> mRunningService = mActivityManager.getRunningServices(intGetTastCounter);
            for (ActivityManager.RunningServiceInfo amService : mRunningService) {
                LogUtils.e("======", "======comPackage:" + amService.service.getPackageName());
                if (0 == amService.service.getPackageName().compareTo(PackageName)) {
                    isStarted = true;
                    break;
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return isStarted;
    }

    public static void getRunningApp(Context context) {
        PackageManager localPackageManager = context.getPackageManager();
        List localList = localPackageManager.getInstalledPackages(0);
        for (int i = 0; i < localList.size(); i++) {
            PackageInfo localPackageInfo1 = (PackageInfo) localList.get(i);
            String str1 = localPackageInfo1.packageName.split(":")[0];
            if (((ApplicationInfo.FLAG_SYSTEM & localPackageInfo1.applicationInfo.flags) == 0)
                    && ((ApplicationInfo.FLAG_UPDATED_SYSTEM_APP & localPackageInfo1.applicationInfo.flags) == 0)
                    && ((ApplicationInfo.FLAG_STOPPED & localPackageInfo1.applicationInfo.flags) == 0)) {
                Log.e("======", "======:" + str1);
            }
        }
    }

    public static boolean isForegroundPkgViaDetectionService(String packageName) {
        return packageName.equals(AccessibilityServiceBase.foregroundPackageName);
    }
}
