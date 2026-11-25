package com.base_util.photopreview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.base_util.R
import com.base_util.databinding.ActivityPhotoPreviewBinding
import com.base_util.util.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

/**
 * 大图预览页：黑色背景，完整显示原图，支持左右滑动切换与双指缩放，点击图片关闭
 * 参数：[EXTRA_URLS]（图片地址列表）、[EXTRA_INDEX]（默认显示第几张）
 */
class PhotoPreviewActivity : BaseActivity<ActivityPhotoPreviewBinding>() {

    companion object {
        private const val STATE_POSITION = "state_position"

        /** 图片地址列表（ArrayList<String>） */
        const val EXTRA_URLS = "extra_urls"

        /** 默认显示的图片下标 */
        const val EXTRA_INDEX = "extra_index"

        /**
         * 打开大图预览
         *
         * @param context 上下文，非 Activity 时自动加 NEW_TASK 标记
         * @param urls 图片地址，支持本地路径、content:// Uri、网络地址
         * @param index 默认显示第几张，越界会自动纠正
         */
        @JvmStatic
        @JvmOverloads
        fun start(context: Context, urls: List<String>?, index: Int = 0) {
            if (urls.isNullOrEmpty()) return
            val intent = Intent(context, PhotoPreviewActivity::class.java)
            intent.putStringArrayListExtra(EXTRA_URLS, ArrayList(urls))
            intent.putExtra(EXTRA_INDEX, index.coerceIn(0, urls.lastIndex))
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    /** 图片地址 */
    private var urls: List<String> = emptyList()

    override fun getLayoutRes(): Int = R.layout.activity_photo_preview

    override fun isImmersiveStatusBar(): Boolean = true

    /** 黑底页面，状态栏图标需要用浅色 */
    override fun isDarkStatusBarText(): Boolean = false

    override fun initView() {
        urls = intent.getStringArrayListExtra(EXTRA_URLS) ?: emptyList()
        if (urls.isEmpty()) {
            finish()
            return
        }
        val startIndex = intent.getIntExtra(EXTRA_INDEX, 0).coerceIn(0, urls.lastIndex)

        dataBinding.vpPhoto.adapter = PhotoAdapter()
        dataBinding.vpPhoto.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                showIndicator(position)
            }
        })
        dataBinding.vpPhoto.currentItem = startIndex
        showIndicator(startIndex)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 旋转/重建后回到之前那一张
        val position = savedInstanceState?.getInt(STATE_POSITION, -1) ?: -1
        if (position in urls.indices) {
            dataBinding.vpPhoto.setCurrentItem(position, false)
            showIndicator(position)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_POSITION, dataBinding.vpPhoto.currentItem)
    }

    /** 页码指示：2/5 */
    private fun showIndicator(position: Int) {
        dataBinding.tvIndicator.text =
            getString(R.string.viewpager_indicator, position + 1, urls.size)
    }

    /**
     * 一页预览：根布局 + 缩放控制器。
     *
     * 注意不能把 attacher 存到 ImageView 的 tag 上，Glide 也用 tag 存自己的 RequestTarget，
     * 两者会互相覆盖（You must not call setTag() on a view Glide is targeting）。
     */
    private class PhotoPage(
        val view: View,
        val imageView: ImageView,
        val attacher: PhotoViewAttacher
    )

    /**
     * content://、file://、http(s):// 统一转成 Uri 交给 Glide（MediaStore 查出来的是 content://），
     * 其余按本地绝对路径处理
     */
    private fun toModel(url: String): Any =
        if (url.contains("://")) Uri.parse(url) else url

    /** 每页一张图：Glide 加载原图，PhotoViewAttacher 提供双指缩放 */
    private inner class PhotoAdapter : PagerAdapter() {

        override fun getCount(): Int = urls.size

        override fun isViewFromObject(view: View, obj: Any): Boolean =
            obj is PhotoPage && view === obj.view

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val itemView = LayoutInflater.from(container.context)
                .inflate(R.layout.item_photo_preview, container, false)
            val imageView = itemView.findViewById<ImageView>(R.id.iv_preview)
            val progress = itemView.findViewById<ProgressBar>(R.id.pb_loading)

            // 双指缩放；点击图片关闭预览（缩放组件会拦截点击事件，需用它的回调）
            val attacher = PhotoViewAttacher(imageView)
            attacher.setOnViewTapListener(object : PhotoViewAttacher.OnViewTapListener {
                override fun onViewTap(view: View, x: Float, y: Float) {
                    finish()
                }
            })

            // 先加入容器再加载，避免图片从缓存瞬间返回时控件还没测量导致贴到左上角
            container.addView(itemView)

            // 不要用 fitCenter()/centerCrop()：缩放由 PhotoViewAttacher 的 Matrix 负责，
            // Glide 的缩放选项会改 ImageView 的 ScaleType，导致双指缩放失效
            Glide.with(imageView)
                .load(toModel(urls[position]))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress.visibility = View.GONE
                        // 等控件测量完成后重新计算，保证居中完整显示
                        imageView.post { attacher.update() }
                        return false
                    }
                })
                .into(imageView)

            return PhotoPage(itemView, imageView, attacher)
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            val page = obj as? PhotoPage ?: return
            // 先取消加载，再释放手势监听，避免滑走后还在回调
            Glide.with(page.imageView).clear(page.imageView)
            page.attacher.cleanup()
            container.removeView(page.view)
        }
    }
}
