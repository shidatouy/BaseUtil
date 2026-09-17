package com.base_util.base


abstract class BasePresenter<V : BaseView, M : BaseModel> {
    protected var mView: V? = null
    protected var mModel: M? = null

    /**
     * 绑定 View 和 Model
     */
    public fun attach(view: V, model: M) {
        mView = view
        mModel = model
    }

    /**
     * 解绑，防止崩溃
     */
    public fun detach() {
        // 取消所有网络请求
        mModel?.cancelRequests()
        // 清除 View 引用
        mView = null
        mModel =null
    }

    /**
     * 检查 View 是否可用
     */
    protected fun isViewAttached(): Boolean {
        return mView != null
    }

    /**
     * 安全地调用 View 回调
     */
    protected fun safeOnSuccess(type: Int, vararg params: Any) {
        if (isViewAttached()) {
            try {
                mView?.onSuccess(type, *params)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 安全地调用 View 失败回调
     */
    protected fun safeOnFailed(type: Int, errorCode: Int, errorMsg: String) {
        if (isViewAttached()) {
            try {
                mView?.onFailed(type, errorCode, errorMsg)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}