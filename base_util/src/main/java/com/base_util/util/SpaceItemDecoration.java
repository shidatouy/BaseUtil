package com.base_util.util;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 给列表项之间加间距（第一项不加顶部间距）。
 *
 * 注意：getChildPosition() 已在 API 22 废弃，且返回 NO_POSITION 时会被当成 0，
 * 这里改用 getChildAdapterPosition()。
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        // 第一项与 NO_POSITION(-1) 都不加顶部间距；旧写法 > NO_POSITION 会把第一项也算进去
        if (position > 0) {
            outRect.top = space;
        }
    }
}
