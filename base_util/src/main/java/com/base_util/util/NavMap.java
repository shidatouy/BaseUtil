package com.base_util.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;


public class NavMap {
    private Context context;

    public NavMap(Context context) {
        this.context = context;
    }

    /**
     * 根据包名判断某个app是否安装
     * 百度地图包名：com.baidu.BaiduMap
     * 高德地图包名：com.autonavi.minimap
     *
     * @param packageName
     * @return
     */
    public boolean isAppInstalled(String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }

        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    public void selectMap2(String address, String latitude, String longitude) {
        if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)) {
            T.showShort(context, "获取位置失败！");
            return;
        }
        double[] db = gaoDeToBaidu(Double.parseDouble(latitude), Double.parseDouble(longitude));
        if (isAppInstalled("com.autonavi.minimap")) {//高德地图
            goToBGaoDeMap(address, Double.parseDouble(latitude), Double.parseDouble(longitude));
        } else if (isAppInstalled("com.baidu.BaiduMap")) {//百度地图
            goToBBaiDuMap(address, db[0] + "", db[1] + "");
        } else if (isAppInstalled("com.tencent.map")) {//腾讯地图
            gotoTengxun(address, db[0], db[1]);
        } else {
            T.showShort(context, "您尚未安装地图！");
        }
    }

    public void selectMap(String address, String latitude, String longitude) {
        if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)) {
            T.showShort(context, "获取位置失败！");
            return;
        }
        double[] db = bdToGaoDe(Double.parseDouble(latitude), Double.parseDouble(longitude));
        if (isAppInstalled("com.baidu.BaiduMap")) {//百度地图
            goToBBaiDuMap(address, latitude, longitude);
        } else if (isAppInstalled("com.autonavi.minimap")) {//高德地图
            goToBGaoDeMap(address, db[0], db[1]);
        } else if (isAppInstalled("com.tencent.map")) {//腾讯地图
            gotoTengxun(address, db[0], db[1]);
        } else {
            T.showShort(context, "您尚未安装地图！");
        }
    }

    /**
     * 跳转到百度地图
     *
     * @param latitude
     * @param longitude
     */
    public void goToBBaiDuMap(String address, String latitude, String longitude) {
        String uri = "baidumap://map/direction"
                + "?origin=我的位置"
                + "&destination=name:" + address
                + "|latlng:" + latitude + "," + longitude
                + "&coord_type=bd09ll"
                + "&mode=driving"
                + "&src=andr.companyName.appName";//src为统计来源必填，companyName、appName是公司名和应用名
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
        intent.addCategory("android.intent.category.DEFAULT");
        context.startActivity(intent);
    }

    /**
     * 跳转到高德地图
     *
     * @param latitude
     * @param longitude
     */
    public void goToBGaoDeMap(String address, double latitude, double longitude) {
        //默认驾车
        String uri = "amapuri://route/plan/"
                + "?dlat=" + latitude + "&dlon=" + longitude
                + "&sname=我的位置"
                + "&dname=" + address
                + "&dev=0"
                + "&t=0";
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }

    /**
     * 启动腾讯地图App进行导航
     *
     * @param address 目的地
     * @param lat     必填 纬度
     * @param lon     必填 经度
     */
    public void gotoTengxun(String address, double lat, double lon) {
        String uri = "qqmap://map/routeplan?type=drive&from=&fromcoord=&to=" + address
                + "&tocoord=" + lat + "," + lon
                + "&policy=0&referer=appName";
        // 启动路径规划页面
        Intent naviIntent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
        naviIntent.addCategory("android.intent.category.DEFAULT");
        naviIntent.setPackage("com.tencent.map");
        context.startActivity(naviIntent);
    }

    /**
     * @author Administrator
     * @time 2018/5/14  13:34
     * @describe 百度转高德（百度坐标bd09ll–>火星坐标gcj02ll）
     */
    public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta);
        double tempLat = z * Math.sin(theta);
        double[] gps = {tempLat, tempLon};
        return gps;
    }

    /**
     * @author Administrator
     * @time 2018/5/14  13:35
     * @describe 高德(腾讯)转百度（火星坐标gcj02ll–>百度坐标bd09ll）
     */
    public static double[] gaoDeToBaidu(double gd_lat, double gd_lon) {
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        double[] gps = {tempLat, tempLon};
        return gps;
    }

}

