package com.base_util.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Activity 管理类
 *
 * 注意：finishActivity() 系列会真正调用 Activity.finish()，
 * 旧实现只把引用从栈里移除，导致「页面没关掉」的问题。
 */
public class AppManager {
    // Activity 栈
    private static final Stack<Activity> activityStack = new Stack<>();
    // 单例模式
    private static volatile AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }

    /**
     * 加入 Activity 到堆栈
     */
    public void addActivity(Activity activity) {
        if (activity == null) return;
        if (!activityStack.contains(activity)) {
            activityStack.add(activity);
        }
    }

    /**
     * 获取当前 Activity（堆栈中最后一个压入的），栈空时返回 null
     */
    public Activity currentActivity() {
        return activityStack.isEmpty() ? null : activityStack.lastElement();
    }

    /**
     * 结束当前 Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        finishActivity(currentActivity());
    }

    /**
     * 仅从栈里移除引用，不调用 finish()。
     *
     * Activity.onDestroy() 里必须用这个方法：
     * 屏幕旋转等配置变更时也会走 onDestroy，此时 isFinishing() 为 false，
     * 若用 finishActivity() 会顺手 finish 掉正在重建的页面。
     */
    public void removeActivity(Activity activity) {
        if (activity == null) return;
        activityStack.remove(activity);
    }

    /**
     * 结束指定的 Activity
     */
    public void finishActivity(Activity activity) {
        if (activity == null) return;
        activityStack.remove(activity);
        if (!activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * 结束指定类名的 Activity
     */
    public void finishActivity(Class<?> cls) {
        if (cls == null) return;
        // 复制一份再遍历，避免边遍历边 remove 触发 ConcurrentModificationException
        List<Activity> snapshot = new ArrayList<>(activityStack);
        for (Activity activity : snapshot) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束全部 Activity
     */
    public void finishAllActivity() {
        List<Activity> snapshot = new ArrayList<>(activityStack);
        for (Activity activity : snapshot) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 当前栈内 Activity 数量
     */
    public int size() {
        return activityStack.size();
    }
}
