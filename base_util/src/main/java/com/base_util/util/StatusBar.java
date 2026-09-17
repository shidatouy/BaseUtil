package com.base_util.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

public class StatusBar {

    private static final int COLOR_TRANSPARENT = 0;

    // ==================== 状态栏颜色 ====================

    /**
     * 设置状态栏颜色
     */
    public static void setStatusBarColor(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 设置状态栏颜色（资源 ID）
     */
    public static void setStatusBarColorRes(Activity activity, @ColorRes int colorRes) {
        setStatusBarColor(activity, activity.getResources().getColor(colorRes));
    }

    // ==================== 透明状态栏 ====================

    /**
     * 使用视图的背景色作为状态栏颜色
     */
    public static void immersive(Activity activity, View v, Boolean darkMode) {
        if (v.getBackground() instanceof ColorDrawable) {
            ColorDrawable background = (ColorDrawable) v.getBackground();
            immersive(activity, background.getColor(), darkMode);
        }
    }

    public static void immersive(Activity activity, View v) {
        immersive(activity, v, null);
    }

    /**
     * 设置透明状态栏或状态栏颜色
     */
    @SuppressLint("ObsoleteSdkInt")
    public static void immersive(Activity activity, @ColorInt int color, Boolean darkMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (color == COLOR_TRANSPARENT) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                activity.getWindow().setStatusBarColor(color);
            } else {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                activity.getWindow().setStatusBarColor(color);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (color != COLOR_TRANSPARENT) {
                setTranslucentView((ViewGroup) activity.getWindow().getDecorView(), activity, color);
            }
        }
        if (darkMode != null) {
            darkMode(activity, darkMode);
        }
    }

    public static void immersive(Activity activity, @ColorInt int color) {
        immersive(activity, color, null);
    }

    /**
     * 退出沉浸式状态栏
     */
    public static void immersiveExit(Activity activity) {
        immersiveExit(activity, false);
    }

    public static void immersiveExit(Activity activity, boolean black) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (black) {
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                } else {
                    TypedValue typedValue = new TypedValue();
                    activity.getTheme().resolveAttribute(android.R.attr.statusBarColor, typedValue, true);
                    activity.getWindow().setStatusBarColor(typedValue.data);
                }
            }
        }
    }

    /**
     * 获取颜色资源值来设置状态栏
     */
    public static void immersiveRes(Activity activity, @ColorRes int color, Boolean darkMode) {
        immersive(activity, activity.getResources().getColor(color), darkMode);
    }

    public static void immersiveRes(Activity activity, @ColorRes int color) {
        immersiveRes(activity, color, null);
    }

    // ==================== 暗色模式 ====================

    /**
     * 开关状态栏暗色模式
     */
    public static void darkMode(Activity activity, boolean darkMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int systemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
            if (darkMode) {
                systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
        }
    }

    public static void darkMode(Activity activity) {
        darkMode(activity, true);
    }

    // ==================== 间距 ====================

    /**
     * 增加 View 的 paddingTop，高度为状态栏高度
     */
    public static void statusPadding(View view) {
        statusPadding(view, false);
    }

    public static void statusPadding(View view, boolean remove) {
        if (view instanceof RelativeLayout) {
            throw new UnsupportedOperationException("Unsupported set statusPadding for RelativeLayout");
        }
        if (Build.VERSION.SDK_INT >= 19) {
            int statusBarHeight = getStatusBarHeight(view.getContext());
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp != null && lp.height > 0) {
                lp.height += statusBarHeight;
            }
            if (remove) {
                if (view.getPaddingTop() < statusBarHeight) return;
                view.setPadding(
                        view.getPaddingLeft(),
                        view.getPaddingTop() - statusBarHeight,
                        view.getPaddingRight(),
                        view.getPaddingBottom()
                );
            } else {
                if (view.getPaddingTop() >= statusBarHeight) return;
                view.setPadding(
                        view.getPaddingLeft(),
                        view.getPaddingTop() + statusBarHeight,
                        view.getPaddingRight(),
                        view.getPaddingBottom()
                );
            }
        }
    }

    /**
     * 增加 View 的 marginTop，高度为状态栏高度
     */
    public static void statusMargin(View view) {
        statusMargin(view, false);
    }

    public static void statusMargin(View view, boolean remove) {
        if (Build.VERSION.SDK_INT >= 19) {
            int statusBarHeight = getStatusBarHeight(view.getContext());
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            if (remove) {
                if (lp.topMargin < statusBarHeight) return;
                lp.topMargin -= statusBarHeight;
            } else {
                if (lp.topMargin >= statusBarHeight) return;
                lp.topMargin += statusBarHeight;
            }
            view.setLayoutParams(lp);
        }
    }

    /**
     * 创建假的透明栏
     */
    private static void setTranslucentView(ViewGroup container, Context context, int color) {
        if (Build.VERSION.SDK_INT >= 19) {
            View simulateStatusBar = container.findViewById(android.R.id.custom);
            if (simulateStatusBar == null && color != 0) {
                simulateStatusBar = new View(container.getContext());
                simulateStatusBar.setId(android.R.id.custom);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(context)
                );
                container.addView(simulateStatusBar, lp);
            }
            if (simulateStatusBar != null) {
                simulateStatusBar.setBackgroundColor(color);
            }
        }
    }

    // ==================== ActionBar ====================

    public static void setActionBarBackground(AppCompatActivity activity, @ColorInt int color) {
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    public static void setActionBarBackgroundRes(AppCompatActivity activity, @ColorRes int color) {
        setActionBarBackground(activity, activity.getResources().getColor(color));
    }

    public static void setActionBarTransparent(AppCompatActivity activity) {
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    // ==================== 辅助 ====================

    /**
     * 显示或隐藏导航栏
     */
    public static void setNavigationBar(Activity activity) {
        setNavigationBar(activity, true);
    }

    public static void setNavigationBar(Activity activity, boolean enabled) {
        if (Build.VERSION.SDK_INT >= 12 && Build.VERSION.SDK_INT <= 18) {
            if (enabled) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.GONE);
            }
        } else if (Build.VERSION.SDK_INT >= 19) {
            int systemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
            if (enabled) {
                systemUiVisibility &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION & ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            } else {
                systemUiVisibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
        }
    }

    /**
     * 设置是否全屏
     */
    public static void setFullscreen(Activity activity) {
        setFullscreen(activity, true);
    }

    public static void setFullscreen(Activity activity, boolean enabled) {
        int systemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
        if (enabled) {
            systemUiVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        } else {
            systemUiVisibility &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    /**
     * 是否有导航栏
     */
    public static boolean isNavigationBar(Activity activity) {
        if (activity == null) return false;
        ViewGroup vp = (ViewGroup) activity.getWindow().getDecorView();
        if (vp != null) {
            for (int i = 0; i < vp.getChildCount(); i++) {
                if (vp.getChildAt(i).getId() != -1) {
                    try {
                        String name = activity.getResources().getResourceEntryName(vp.getChildAt(i).getId());
                        if ("navigationBarBackground".equals(name)) {
                            return true;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return false;
    }

    /**
     * 导航栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        if (context == null) return 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        if (context == null) return 0;
        int result = 24;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        } else {
            result = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    result,
                    Resources.getSystem().getDisplayMetrics()
            );
        }
        return result;
    }
}
