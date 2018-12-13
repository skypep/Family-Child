package com.toro.helper.view.iphone;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.toro.helper.R;

import java.util.ArrayList;

/**
 * Create By liujia
 * on 2018/10/24.
 **/
public class IphoneDialogBottomMenu extends Dialog {

    private Context mContext;
    private ArrayList<String> mMenuItems;
    private MenuItemOnClickListener mMenuItemOnClickListener;
    private RecyclerView mRecyclerView;
    private MenuItemAdapter mAdapter;

    public IphoneDialogBottomMenu(Context context, ArrayList<String> menuItem, MenuItemOnClickListener menuItemOnClickListener) {
        super(context);
        mContext = context;
        mMenuItems = menuItem;
        mMenuItemOnClickListener = menuItemOnClickListener;
        init();
    }

    /**
     * 设置菜单item数据
     *
     * @param menuItem
     */
    private void setMenuItems(ArrayList<String> menuItem) {
        mMenuItems = menuItem;
        mAdapter = new MenuItemAdapter(mContext, menuItem);
        if (mMenuItemOnClickListener != null) {
            mMenuItemOnClickListener.dialog = this;
            mMenuItemOnClickListener.mMenuItem = menuItem;
            mAdapter.setItemOnClickListener(mMenuItemOnClickListener);
        }
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));// liujia add
        mRecyclerView.setAdapter(mAdapter);
    }

    private void init() {
        initWindow();
        View contentView = View.inflate(mContext, R.layout.iphone_dialog, null);
        setCanceledOnTouchOutside(true);
        setContentView(contentView);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TextView tvCancel = (TextView) contentView.findViewById(R.id.tv_cancle);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        View rootView = findViewById(R.id.dialog_ll);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setMenuItems(mMenuItems);
    }

    /**
     * 初始化window参数
     */
    private void initWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 取消标题
        Window dialogWindow = getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        // 设置显示动画
        dialogWindow.setWindowAnimations(R.style.IphoneDialog);
    }
}
