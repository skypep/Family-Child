package com.toro.helper.fragment.photo;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.toro.helper.R;
import com.toro.helper.activity.MyPhotoActivity;
import com.toro.helper.activity.PhotoPreviewActivity;
import com.toro.helper.app.AppConfig;
import com.toro.helper.modle.photo.PhotoData;
import com.toro.helper.modle.photo.PhotoItem;
import com.toro.helper.utils.ImageLoad;
import com.toro.helper.view.RecyclerItemDecoration;
import com.toro.helper.view.RoundnessImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By liujia
 * on 2018/10/23.
 **/
public class PhotoViewHolder extends RecyclerView.ViewHolder {

    private RecyclerView recyclerView;
    private RoundnessImageView headImg;
    private TextView nameText,timeText,mRemindTv;
    private ImageView inAction,selectAction;
    private Context mContext;
    private boolean isFirst = false;
    private PhotoItemAdapter adapter;
    private boolean isMyPhotoMode;
    private View rootView;

    public PhotoViewHolder(@NonNull View itemView,List<PhotoItem> photos,boolean isMyphotoMode) {
        super(itemView);
        this.rootView = itemView;
        this.isMyPhotoMode = isMyphotoMode;
        mContext = itemView.getContext();
        recyclerView = itemView.findViewById(R.id.recycler_view);
        headImg = itemView.findViewById(R.id.head_img);
        nameText = itemView.findViewById(R.id.name);
        timeText = itemView.findViewById(R.id.time);
        mRemindTv = itemView.findViewById(R.id.mRemindTv);
        inAction = itemView.findViewById(R.id.action_in);
        selectAction = itemView.findViewById(R.id.action_selecte);
        adapter = new PhotoItemAdapter(photos);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusableInTouchMode(false);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, AppConfig.PhotoSpanCount));
        recyclerView.addItemDecoration(new RecyclerItemDecoration(mContext.getResources().getDimensionPixelOffset(R.dimen.photo_list_photo_offset)));
        isFirst = true;
    }

    public void setViewPool(RecyclerView.RecycledViewPool viewPool) {
        recyclerView.setRecycledViewPool(viewPool);
    }

    public void init(boolean needLoad,final PhotoData data) {
        nameText.setText(data.getUserinfo().getName());
        timeText.setText(data.getGmtUpdated());
        adapter.update(needLoad,data.getPhotos());
        if (data.getRemindNames() == null || data.getRemindNames().size() == 0){
            mRemindTv.setVisibility(View.GONE);
        }else{
            StringBuilder sb = new StringBuilder();
            for (String str : data.getRemindNames()){
                sb.append(str+"  ");
            }
            mRemindTv.setText("提到了:"+sb);
            mRemindTv.setVisibility(View.VISIBLE);
        }
        if(needLoad) {
            ImageLoad.GlidLoad(headImg,data.getUserinfo().getHeadPhoto(),R.mipmap.default_head);
            headImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<PhotoItem> photos = new ArrayList<>();
                    photos.add(data.getUserinfo().getHeadPhoto());
                    mContext.startActivity(PhotoPreviewActivity.createIntent(mContext,photos,0));
                }
            });
        }else {
            headImg.setImageResource(R.mipmap.default_head);
        }
        if(isMyPhotoMode){
            nameText.setVisibility(View.GONE);
            headImg.setVisibility(View.GONE);
            timeText.setTextColor(Color.BLACK);
            inAction.setVisibility(View.GONE);
        }else{
            inAction.setVisibility(View.VISIBLE);
            inAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(MyPhotoActivity.createIntent(mContext,data.getUserinfo().getUid()));
                }
            });
        }
        selectAction.setVisibility(View.GONE);
    }

    public void initEditMode(int index,boolean isSelected,View.OnClickListener listener) {
        inAction.setVisibility(View.GONE);
        selectAction.setVisibility(View.VISIBLE);
        if(isSelected) {
            selectAction.setImageResource(R.mipmap.check_checked);
        } else {
            selectAction.setImageResource(R.mipmap.check_un_check);
        }
        selectAction.setTag(index);
        selectAction.setOnClickListener(listener);
    }

    class PhotoItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<PhotoItem> photos;
        private boolean needload = true;

        PhotoItemAdapter(List<PhotoItem> photos) {
            this.photos = photos;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            return new PhotoItemViewHolder(inflater.inflate(R.layout.photo_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            PhotoItemViewHolder itemView = (PhotoItemViewHolder) viewHolder;
            itemView.setUpPhotoView(needload,photos.get(i));
            itemView.setOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(PhotoPreviewActivity.createIntent(mContext,photos,i));
                }
            });
        }

        public void update(boolean needload,List<PhotoItem> photos){
            this.photos = photos;
            this.needload = needload;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }
    }
    }
