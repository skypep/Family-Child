package com.toro.helper.fragment.photo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.toro.helper.R;
import com.toro.helper.base.ToroListAdapter;
import com.toro.helper.modle.photo.PhotoData;

import java.util.List;

/**
 * Create By liujia
 * on 2018/10/29.
 **/
public class PhotoAdapter1 extends ToroListAdapter {
    private RecyclerView.RecycledViewPool viewPool; // 优化嵌套recycleView 性能

    @Override
    protected RecyclerView.ViewHolder onCreateView(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        PhotoData data = (PhotoData) datas.get(i);
        return new PhotoViewHolder(inflater.inflate(R.layout.photo_data, viewGroup, false),data.getPhotos(),false);
    }

    @Override
    public void onBindView(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        PhotoViewHolder photoHolder = (PhotoViewHolder) viewHolder;
        photoHolder.setViewPool(viewPool);
        PhotoData data = (PhotoData) datas.get(i);
        photoHolder.init(needLoad,data);
    }

    public PhotoAdapter1(Context context, List<?> datas) {
        super(context, datas);
        viewPool = new RecyclerView.RecycledViewPool();
    }
}
