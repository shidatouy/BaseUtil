package com.base_util.base

import android.content.Context

interface  BaseView {

    /**
     * 获取上下文
     * @return 上下文
     */
    fun getContext(): Context


    /**
     * 成功回调
     * @param type 请求类型，用于区分不同的请求
     * @param params 请求成功返回的数据
     */
    fun onSuccess(type: Int, vararg params: Any?)

    /**
     * 失败回调
     * @param type 请求类型，用于区分不同的请求
     * @param errorCode 错误码
     * @param errorMsg 错误信息
     */
    fun onFailed(type: Int, errorCode: Int, errorMsg: String)
}