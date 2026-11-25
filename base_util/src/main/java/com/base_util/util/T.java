package com.base_util.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast 统一管理
 *
 * 单例 Toast 必须用 ApplicationContext 创建，否则会持有已经销毁的 Activity，
 * 在 Android 11+ 上设置文本后可能直接不显示。
 *
 * @author way
 */
public class T {
	// Toast
	private static Toast toast;

	/**
	 * 短时间显示 Toast
	 */
	public static void showShort(Context context, CharSequence message) {
		show(context, message, Toast.LENGTH_SHORT);
	}

	/**
	 * 短时间显示 Toast
	 */
	public static void showShort(Context context, int message) {
		show(context, message, Toast.LENGTH_SHORT);
	}

	/**
	 * 长时间显示 Toast
	 */
	public static void showLong(Context context, CharSequence message) {
		show(context, message, Toast.LENGTH_LONG);
	}

	/**
	 * 长时间显示 Toast
	 */
	public static void showLong(Context context, int message) {
		show(context, message, Toast.LENGTH_LONG);
	}

	/**
	 * 自定义显示 Toast 时间
	 */
	public static void show(Context context, CharSequence message, int duration) {
		Context appContext = appContext(context);
		if (appContext == null) return;
		if (toast == null) {
			toast = Toast.makeText(appContext, message, duration);
		} else {
			toast.setDuration(duration);
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 自定义显示 Toast 时间
	 */
	public static void show(Context context, int message, int duration) {
		Context appContext = appContext(context);
		if (appContext == null) return;
		if (toast == null) {
			toast = Toast.makeText(appContext, message, duration);
		} else {
			toast.setDuration(duration);
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * Hide the toast, if any.
	 */
	public static void hideToast() {
		if (null != toast) {
			toast.cancel();
			toast = null;
		}
	}

	private static Context appContext(Context context) {
		if (context == null) return null;
		Context applicationContext = context.getApplicationContext();
		return applicationContext != null ? applicationContext : context;
	}
}
