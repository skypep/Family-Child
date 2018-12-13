package com.toro.helper.fragment.photo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toro.helper.R;
import com.toro.helper.app.AppConfig;
import com.toro.helper.modle.photo.PhotoData;

import java.util.List;

/**
 * Create By liujia
 * on 2018/10/24.
 **/
public class PhotoEditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> images;
    private Context mContext;

    private View.OnClickListener deleteListener;
    private View.OnClickListener plusListener;

    private static final int IMAGE_TYPE = 1;
    private static final int PLUS_TYPE = 2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new PhotoItemViewHolder(inflater.inflate(R.layout.photo_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        PhotoItemViewHolder itemView = (PhotoItemViewHolder) viewHolder;
        if(getItemViewType(i) == PLUS_TYPE) {
            itemView.setUpEditPlusView(plusListener);
        } else if(getItemViewType(i) == IMAGE_TYPE) {
            itemView.setUpEditPhotoView(i,images.get(i),deleteListener);
        }
    }

    public PhotoEditAdapter(Context context, List<String> photoDatas,View.OnClickListener plusListener,View.OnClickListener deleteListener) {
        this.mContext = context;
        this.images = photoDatas;
        this.plusListener = plusListener;
        this.deleteListener = deleteListener;
    }

    public void updateListData(List<String> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= images.size()) {
            return PLUS_TYPE;
        } else {
            return IMAGE_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        if(images.size() < AppConfig.PhotoMaxCoun) {
            return images.size() + 1;
        }
        return images.size();
    }
}
