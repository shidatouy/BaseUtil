package com.module_base.route;


import com.alibaba.android.arouter.launcher.ARouter;

import androidx.fragment.app.Fragment;

/**
 * 路由的工具类
 */

public class RouteUtils {
    public static final String One_Activity = "/one/Activity";
    public static final String One_Fragment_One = "/one/main";
    public static final String Two_Fragment_One = "/two/main";
    public static final String Three_Fragment_One = "/three/main";
    public static final String BZ_Idcard = "/bz/card";
    public static final String BZ_Yxmain = "/bz/yxmain";
    public static final String BZ_List = "/xb/bzlist";
    public static final String BZ_Progress = "/pr/progress";
    public static final String BZ_Xietong = "/xt/xietong";
    public static final String BZ_Zhuanfa = "/zf/zhuanfa";
    public static final String KY_Bibu = "/ky/bilu";
    public static final String WZ_Banli = "/wz/banli";
    public static final String WL_Main = "/wl/wuliu";
    public static final String Sus_Person = "/sus/person";
    public static final String Sus_Car = "/sus/car";


    public static Fragment getOneFragment() {
        Fragment fragment = (Fragment) ARouter.getInstance().build(One_Fragment_One).navigation();
        return fragment;
    }

    public static Fragment getTwoFragment() {
        Fragment fragment = (Fragment) ARouter.getInstance().build(Two_Fragment_One).navigation();
        return fragment;
    }

    public static Fragment getThreeFragment() {
        Fragment fragment = (Fragment) ARouter.getInstance().build(Three_Fragment_One).navigation();
        return fragment;
    }

    /**
     * 跳转到新办
     */
    public static void startXinBan() {
        ARouter.getInstance().build(BZ_Idcard).navigation();
    }

    /**
     * 跳转到延续
     */
    public static void startYanXun() {
        ARouter.getInstance().build(BZ_Yxmain).navigation();
    }

    /**
     * 跳转到微信办证
     */
    public static void startWxList() {
        ARouter.getInstance().build(BZ_List).navigation();
    }

    /**
     * 跳转到证件办理流程列表
     */
    public static void startProgressList() {
        ARouter.getInstance().build(BZ_Progress).navigation();
    }

    /**
     * 跳转到勘验流程列表
     */
    public static void startKanYanList() {
        ARouter.getInstance().build(KY_Bibu).navigation();
    }

    /**
     * 跳转到无证监管列表
     */
    public static void startWuZList() {
        ARouter.getInstance().build(WZ_Banli).navigation();
    }

    /**
     * 跳转到物流添加
     */
    public static void startWuLiu() {
        ARouter.getInstance().build(WL_Main).navigation();
    }

    /**
     * 跳转到可以人员
     */
    public static void startSusperson() {
        ARouter.getInstance().build(Sus_Person).navigation();
    }

    /**
     * 跳转到可疑车辆
     */
    public static void startSuscar() {
        ARouter.getInstance().build(Sus_Car).navigation();
    }

}
