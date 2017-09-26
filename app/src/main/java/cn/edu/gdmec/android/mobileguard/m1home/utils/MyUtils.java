package cn.edu.gdmec.android.mobileguard.m1home.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2017/9/24.
 */
//获取本地版本号
    //返回本地版本号
public class MyUtils {
    public static String getVersion(Context context) {
        //packageManager 可以获取清单文件中的所有信息
        PackageManager packageManager = context.getPackageManager();
        try {
            //获取到当前程序的包名。
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
