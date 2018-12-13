package com.toro.helper.view.iphone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toro.helper.R;
import com.toro.helper.fragment.photo.PhotoViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By liujia
 * on 2018/10/24.
 **/
public class MenuItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<String> menuItems;
    private MenuItemOnClickListener itemOnClickListener;

    MenuItemAdapter(Context context,ArrayList<String> menuItems) {
        this.mContext = context;
        this.menuItems = menuItems;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new IphoneMenuItemHolderView(inflater.inflate(R.layout.iphone_dialog_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        IphoneMenuItemHolderView itemView = (IphoneMenuItemHolderView) viewHolder;
        itemView.setupView(i,menuItems.get(i),itemOnClickListener);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public void setItemOnClickListener(MenuItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }
}
