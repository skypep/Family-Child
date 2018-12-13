package com.toro.helper.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Create By liujia
 * on 2018/10/24.
 **/
public class RecyclerItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public RecyclerItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.bottom = space;
    }
}
