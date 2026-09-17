package com.base_util.base

import com.qq.okhttp.request.RequestCall
import java.util.*

interface BaseModel {
    /**
     * 取消这个 Model 的所有请求
     */
    fun cancelRequests()
}

/**
 * BaseModel 的基础实现
 */
 abstract class BaseModelImpl : BaseModel {
    private val requests = ArrayList<RequestCall>()

    override fun cancelRequests() {
        for (call in requests) {
            call.cancel()
        }
        requests.clear()
    }

    /**
     * 添加请求到管理列表
     */
    protected fun addRequest(call: RequestCall) {
        requests.add(call)
    }

    /**
     * 创建网络请求并自动管理
     */
    protected fun createRequest(call: RequestCall): RequestCall {
        requests.add(call)
        return call
    }
}