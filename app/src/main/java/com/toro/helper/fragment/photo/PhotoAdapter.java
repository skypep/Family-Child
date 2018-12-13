package com.toro.helper.fragment.photo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toro.helper.R;
import com.toro.helper.modle.photo.PhotoData;

import java.util.List;

/**
 * Create By liujia
 * on 2018/10/23.
 **/
public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PhotoData> photoDatas;
    private Context mContext;
    private RecyclerView.RecycledViewPool viewPool; // 优化嵌套recycleView 性能
    private boolean needLoad = true;
    private boolean isMyPhotoMode;
    private boolean isEditMode;
    private View.OnClickListener checkOnclickListener;
    private boolean[]deleteChecks;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        PhotoViewHolder viewHolder = new PhotoViewHolder(inflater.inflate(R.layout.photo_data, viewGroup, false),photoDatas.get(i).getPhotos(),isMyPhotoMode);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        PhotoViewHolder photoHolder = (PhotoViewHolder) viewHolder;
        photoHolder.setViewPool(viewPool);
        photoHolder.init(needLoad,photoDatas.get(i));
        if(isEditMode) {
            photoHolder.initEditMode(i,deleteChecks[i],checkOnclickListener);
        }
    }

    public PhotoAdapter(Context context, List<PhotoData> photoDatas,boolean isMyPhotoMode) {
        this.isMyPhotoMode = isMyPhotoMode;
        this.mContext = context;
        this.photoDatas = photoDatas;
        viewPool = new RecyclerView.RecycledViewPool();
    }

    public void updatePhotoDatas(List<PhotoData> photoDatas) {
        this.photoDatas = photoDatas;
        notifyDataSetChanged();
    }

    public void onLoadImageSwitch(boolean flag) {
        needLoad = flag;
        if(needLoad) {
            notifyDataSetChanged();
        }
    }

    public void enterEditMode(boolean[]deleteChecks, View.OnClickListener listener) {
        isEditMode = true;
        this.deleteChecks = deleteChecks;
        this.checkOnclickListener = listener;
        notifyDataSetChanged();
    }

    public void updateEditMode(boolean[]deleteChecks,List<PhotoData> photoDatas) {
        this.deleteChecks = deleteChecks;
        updatePhotoDatas(photoDatas);
    }

    public void exitEditMode() {
        isEditMode = false;
        notifyDataSetChanged();
    }

    public void updateEditCheckBox(boolean[]deleteChecks) {
        this.deleteChecks = deleteChecks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return photoDatas == null? 0 : photoDatas.size();
    }
}
