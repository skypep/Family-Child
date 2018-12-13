package com.toro.helper.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.toro.helper.R;
import com.toro.helper.fragment.photo.PhotoViewHolder;

import java.util.List;

/**
 * Create By liujia
 * on 2018/10/29.
 **/
public abstract class ToroListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<?> datas;
    protected Context mContext;
    protected boolean needLoad = true;

    protected abstract RecyclerView.ViewHolder onCreateView(@NonNull ViewGroup viewGroup, int i);
    public abstract void onBindView(@NonNull RecyclerView.ViewHolder viewHolder, int i);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return onCreateView(viewGroup,i);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        onBindView(viewHolder,i);
    }

    public ToroListAdapter(Context context, List<?> datas) {
        this.mContext = context;
        this.datas = datas;
    }

    public void updateDatas(List<?> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void onLoadImageSwitch(boolean flag) {
        needLoad = flag;
        if(needLoad) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return datas == null? 0 : datas.size();
    }
}
