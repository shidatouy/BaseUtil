package com.base_util.base

data class PageResult<T>(
    val list: List<T>,
    val totalCount: Int,
)