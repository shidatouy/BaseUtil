package com.base_util.error;

import android.content.Context;

import com.base_util.R;
import com.base_util.util.T;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;

public class HandelException {
    public static void exceptionMsg(Context contex, Exception exception) {
        if (exception instanceof NetworkError || exception instanceof ConnectException || exception instanceof SocketException) {// 网络不好
            T.showShort(contex, R.string.error_please_check_network);
        } else if (exception instanceof TimeoutError) {// 请求超时
            T.showShort(contex, R.string.error_timeout);
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
            T.showShort(contex, R.string.error_not_found_server);
        } else if (exception instanceof URLError) {// URL是错的
            T.showShort(contex, R.string.error_url_error);
        } else if (exception instanceof NotFoundCacheError) {
            T.showShort(contex, R.string.error_not_found_cache);
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
        } else if (exception instanceof IOException) {
//            T.showShort(contex, R.string.error_io); // IO 异常，涉及到文件读取，发生异常，一般是文件位置错误
        } else {
            T.showShort(contex, R.string.error_unknow);
        }
    }

    public static void exceptionCode(Context contex, String code, String msg) {
        T.showShort(contex, msg);
        return;
    }
}
