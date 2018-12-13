package com.toro.helper.view.iphone;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.toro.helper.R;

/**
 * Create By liujia
 * on 2018/10/24.
 **/
public class IphoneMenuItemHolderView extends RecyclerView.ViewHolder {

    private TextView itemText;
    private View rootView;

    public IphoneMenuItemHolderView(@NonNull View itemView) {
        super(itemView);
        this.rootView = itemView;
        itemText = itemView.findViewById(R.id.item_text);
    }

    public void setupView(int index,String itemText,MenuItemOnClickListener itemOnClickListener) {
        this.itemText.setText(itemText);
        rootView.setTag(index);
        rootView.setOnClickListener(itemOnClickListener);
    }
}
