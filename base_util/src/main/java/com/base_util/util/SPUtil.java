package com.base_util.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SPUtil {
    public static  final String  APPNAME = "pdsyc";
    public static void putString(Context context, String key, String s) {
        SharedPreferences sp = context.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, s);
        editor.commit();
    }

    public static void clearSp(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static void removeKey(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void putInt(Context context, String key, int s) {
        SharedPreferences sp = context.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(key, s);
        editor.commit();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    /**
     * ��booleanֵ
     *
     * @param context ---key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * ȡbooleanֵ
     *
     * @param context ---key
     * @param value
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, value);
    }

    /**
     * ȡStringֵ
     *
     * @param context ---key
     * @param value
     * @return
     */
    public static String getString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, value);
    }

    //获取用户id
    public static String getUserId(Context context) {
        return SPUtil.getString(context, "userId");
    }
    //获取密码
    public static String getPaseword(Context context) {
        return SPUtil.getString(context, "password");
    }
    //获取用户名字
    public static String getUserName(Context context) {
        return SPUtil.getString(context, "userName");
    }

    //获取登录密码
    public static String getPassWord(Context context) {
        return SPUtil.getString(context, "password");
    }

    //获取手机号
    public static String getPhone(Context context) {
        return SPUtil.getString(context, "phone");
    }

    //获取session
    public static String getSession(Context context) {
        return SPUtil.getString(context, "session");
    }

    public static String getCompany(Context context) {
        return SPUtil.getString(context, "company");
    }

    public static String getCompanyId(Context context) {
        return SPUtil.getString(context, "companyId");
    }
    public static String getCompanyName(Context context) {
        return SPUtil.getString(context, "companyName");
    }
    //获取区域编码
    public static String getArea(Context context) {
        return SPUtil.getString(context, "area");
    }
    //cer
    public static String getCer(Context context) {
        return SPUtil.getString(context, "cer");
    }

    //leCertNo
    public static String getleCertNo(Context context) {
        return SPUtil.getString(context, "leCertNo");
    }

    public static String getDeptId(Context context) {
        return SPUtil.getString(context, "deptId");
    }
    public static String getDeptName(Context context) {
        return SPUtil.getString(context, "deptName");
    }

    //获取画板key
    public static String getPanit(Context context) {
        return SPUtil.getString(context, "panit");
    }

    public static String getChecke(Context context) {
        return SPUtil.getString(context, "checke");
    }

    public static String getOrgName(Context context) {
        return SPUtil.getString(context, "orgName");
    }

}


