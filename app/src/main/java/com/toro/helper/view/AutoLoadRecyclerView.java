package com.toro.helper.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Create By liujia
 * on 2018/10/27.
 **/
public class AutoLoadRecyclerView extends RecyclerView {
    private onLoadMoreListener loadMoreListener;    //加载更多回调
    private boolean isLoadingMore;                  //是否加载更多
    private OnLoadImageSwitchListener loadImageSwitchListener; // 是否需要加载图片回调

    public AutoLoadRecyclerView(Context context) {
        this(context, null);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        isLoadingMore = false;  //默认无需加载更多
    }

    /**
     * 配置显示图片，需要设置这几个参数，快速滑动时，暂停图片加载
     *
     * @param pauseOnScroll  当屏幕滚动且用户使用的触碰或手指还在屏幕上时 是否加载
     * @param pauseOnFling 由于用户的操作，屏幕产生惯性滑动时为时 是否加载
     */
    public void setOnPauseListenerParams(OnLoadImageSwitchListener loadImageSwitchListener,boolean pauseOnScroll, boolean pauseOnFling) {
        this.loadImageSwitchListener = loadImageSwitchListener;
        setOnScrollListener(new AutoLoadScrollListener(loadImageSwitchListener,pauseOnScroll, pauseOnFling));

    }

    public void setLoadMoreListener(onLoadMoreListener loadMoreListener) {
        isLoadingMore = false;
        this.loadMoreListener = loadMoreListener;
    }

    //加载更多的回调接口
    public interface onLoadMoreListener {
        void loadMore();
    }

    //是否加载图片开关接口
    public interface OnLoadImageSwitchListener {
        void onLoadImageSwitch(boolean flag);
    }

    /**
     * 滑动自动加载监听器
     */
    private class AutoLoadScrollListener extends OnScrollListener {

        private final boolean pauseOnScroll;
        private final boolean pauseOnFling;
        private final OnLoadImageSwitchListener onLoadImageSwitchListener;

        public AutoLoadScrollListener(OnLoadImageSwitchListener onLoadImageSwitchListener,boolean pauseOnScroll, boolean pauseOnFling) {
            super();
            this.onLoadImageSwitchListener = onLoadImageSwitchListener;
            this.pauseOnScroll = pauseOnScroll;
            this.pauseOnFling = pauseOnFling;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            //由于GridLayoutManager是LinearLayoutManager子类，所以也适用
            if (getLayoutManager() instanceof LinearLayoutManager) {
                int lastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = AutoLoadRecyclerView.this.getAdapter().getItemCount();

                //有回调接口，且不是加载状态，且计算后剩下2个item，且处于向下滑动，则自动加载
                if (loadMoreListener != null && !isLoadingMore && lastVisibleItem >= totalItemCount -
                        2 && dy > 0) {
                    loadMoreListener.loadMore();
                    isLoadingMore = true;
                }
            }
        }

        //当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；由于用户的操作，屏幕产生惯性滑动时为2
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            //根据newState状态做处理
                switch (newState) {
                    case 0:
                        onLoadImageSwitchListener.onLoadImageSwitch(true);
                        break;

                    case 1:
                        onLoadImageSwitchListener.onLoadImageSwitch(!pauseOnScroll);
                        break;

                    case 2:
                        onLoadImageSwitchListener.onLoadImageSwitch(!pauseOnFling);
                        break;
                }
            }
        }
    }
