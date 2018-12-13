package com.toro.helper.view.iphone;

import android.app.Dialog;
import android.view.View;

import java.util.ArrayList;

/**
 * Create By liujia
 * on 2018/10/24.
 **/
public abstract class MenuItemOnClickListener implements View.OnClickListener {

    public Dialog dialog;
    public ArrayList<String> mMenuItem;

    @Override
    public void onClick(View v) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        int index = -1;
        String item = "";
        if (mMenuItem != null && !mMenuItem.isEmpty()) {
            index = (int) v.getTag();
            item = mMenuItem.get(index);
        }
        onClickMenuItem(v, index, item);
    }

    /**
     * 菜单点击事件回调
     *
     * @param v
     * @param item_index 点击item的下标
     * @param item       点击item的字符串
     */
    public abstract void onClickMenuItem(View v, int item_index, String item);
}
