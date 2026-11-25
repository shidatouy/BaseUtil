package com.base_util.util;

import android.os.Parcelable;
import android.util.Log;

import com.tencent.mmkv.MMKV;

import java.util.Collections;
import java.util.Set;

/**
 * MMKV 的静态封装。
 *
 * 使用前必须在 Application 里调用 {@code MMKV.initialize(context)}（BaseApplication 已处理）。
 *
 * 旧实现把 MMKV 实例只在 {@link #getInstance()} 的构造器里赋值，若调用方直接调
 * {@code MMkvUtil.encode(...)} 而没先 getInstance()，mv 仍是 null 会直接 NPE；
 * 这里每个方法都走 {@link #kv()} 兜底，取不到实例时安全返回。
 */
public class MMkvUtil {

    private static final String TAG = "MMkvUtil";

    private static volatile MMkvUtil mInstance;
    private static volatile MMKV mv;

    private MMkvUtil() {
    }

    /**
     * 初始化 MMKV，只需要初始化一次，建议在 Application 中调用
     */
    public static MMkvUtil getInstance() {
        if (mInstance == null) {
            synchronized (MMkvUtil.class) {
                if (mInstance == null) {
                    mInstance = new MMkvUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 取 MMKV 实例；忘记 initialize 时返回 null 而不是抛异常
     */
    private static MMKV kv() {
        if (mv != null) {
            return mv;
        }
        synchronized (MMkvUtil.class) {
            if (mv == null) {
                try {
                    mv = MMKV.defaultMMKV();
                } catch (Exception e) {
                    Log.e(TAG, "MMKV 不可用，请先在 Application 中调用 MMKV.initialize(context)", e);
                }
            }
        }
        return mv;
    }

    /**
     * 按值的实际类型保存数据
     */
    public static void encode(String key, Object object) {
        MMKV store = kv();
        if (store == null || key == null || object == null) {
            return;
        }
        if (object instanceof String) {
            store.encode(key, (String) object);
        } else if (object instanceof Integer) {
            store.encode(key, (Integer) object);
        } else if (object instanceof Boolean) {
            store.encode(key, (Boolean) object);
        } else if (object instanceof Float) {
            store.encode(key, (Float) object);
        } else if (object instanceof Long) {
            store.encode(key, (Long) object);
        } else if (object instanceof Double) {
            store.encode(key, (Double) object);
        } else if (object instanceof byte[]) {
            store.encode(key, (byte[]) object);
        } else {
            store.encode(key, object.toString());
        }
    }

    public static void encodeSet(String key, Set<String> sets) {
        MMKV store = kv();
        if (store == null || key == null || sets == null) {
            return;
        }
        store.encode(key, sets);
    }

    public static void encodeParcelable(String key, Parcelable obj) {
        MMKV store = kv();
        if (store == null || key == null || obj == null) {
            return;
        }
        store.encode(key, obj);
    }

    public static Integer decodeInt(String key) {
        MMKV store = kv();
        return store == null ? 0 : store.decodeInt(key, 0);
    }

    public static Double decodeDouble(String key) {
        MMKV store = kv();
        return store == null ? 0.00 : store.decodeDouble(key, 0.00);
    }

    public static Long decodeLong(String key) {
        MMKV store = kv();
        return store == null ? 0L : store.decodeLong(key, 0L);
    }

    public static Boolean decodeBoolean(String key) {
        MMKV store = kv();
        return store != null && store.decodeBool(key, false);
    }

    public static Float decodeFloat(String key) {
        MMKV store = kv();
        return store == null ? 0F : store.decodeFloat(key, 0F);
    }

    public static byte[] decodeBytes(String key) {
        MMKV store = kv();
        return store == null ? null : store.decodeBytes(key);
    }

    public static String decodeString(String key) {
        MMKV store = kv();
        return store == null ? "" : store.decodeString(key, "");
    }

    public static Set<String> decodeStringSet(String key) {
        MMKV store = kv();
        return store == null
                ? Collections.<String>emptySet()
                : store.decodeStringSet(key, Collections.<String>emptySet());
    }

    public static Parcelable decodeParcelable(String key) {
        MMKV store = kv();
        return store == null ? null : store.decodeParcelable(key, null);
    }

    /**
     * 移除某个 key
     */
    public static void removeKey(String key) {
        MMKV store = kv();
        if (store == null || key == null) {
            return;
        }
        store.removeValueForKey(key);
    }

    /**
     * 清除所有 key
     */
    public static void clearAll() {
        MMKV store = kv();
        if (store == null) {
            return;
        }
        store.clearAll();
    }
}
