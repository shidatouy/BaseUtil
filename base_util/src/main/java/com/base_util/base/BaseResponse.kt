package com.base_util.base

// 响应实体
data class BaseResponse<T>(
    val code: Int,
    val message: String,
    val data: T? = null,
    val count: Int = 0,// 请求列表的时候传回来的数据总条数
)
