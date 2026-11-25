package com.base_util.util;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 文件下载工具类（单例）。
 *
 * 回调统一切到主线程，可以直接更新 UI。
 * Created on 2017/10/16.
 */
public class DownloadUtil {

    private static volatile DownloadUtil downloadUtil;

    private final OkHttpClient okHttpClient;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            synchronized (DownloadUtil.class) {
                if (downloadUtil == null) {
                    downloadUtil = new DownloadUtil();
                }
            }
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    /**
     * @param url          下载连接
     * @param destFileDir  下载的文件储存目录
     * @param destFileName 下载文件名称
     * @param listener     下载监听（回调在主线程）
     */
    public void download(final String url, final String destFileDir, final String destFileName,
                         final OnDownloadListener listener) {
        if (url == null || listener == null) {
            return;
        }
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                postFailed(listener, e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                ResponseBody body = response.body();
                if (!response.isSuccessful() || body == null) {
                    postFailed(listener, new IOException("下载失败，HTTP " + response.code()));
                    return;
                }

                File dir = new File(destFileDir);
                if (!dir.exists() && !dir.mkdirs()) {
                    postFailed(listener, new IOException("创建目录失败: " + destFileDir));
                    return;
                }
                File file = new File(dir, destFileName);

                InputStream is = null;
                FileOutputStream fos = null;
                byte[] buf = new byte[8192];
                int len;
                long sum = 0;
                // contentLength 可能为 -1（chunked），此时不回调进度
                long total = body.contentLength();
                try {
                    is = body.byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        if (total > 0) {
                            int progress = (int) (sum * 100 / total);
                            postProgress(listener, progress);
                        }
                    }
                    fos.flush();
                    postSuccess(listener, file);
                } catch (Exception e) {
                    postFailed(listener, e);
                } finally {
                    closeQuietly(is);
                    closeQuietly(fos);
                }
            }
        });
    }

    private void postSuccess(final OnDownloadListener listener, final File file) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onDownloadSuccess(file);
            }
        });
    }

    private void postProgress(final OnDownloadListener listener, final int progress) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onDownloading(progress);
            }
        });
    }

    private void postFailed(final OnDownloadListener listener, final Exception e) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onDownloadFailed(e);
            }
        });
    }

    private static void closeQuietly(java.io.Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }

    public interface OnDownloadListener {
        /**
         * @param file 下载成功后的文件
         */
        void onDownloadSuccess(File file);

        /**
         * @param progress 下载进度 0-100
         */
        void onDownloading(int progress);

        /**
         * @param e 下载异常信息
         */
        void onDownloadFailed(Exception e);
    }
}
