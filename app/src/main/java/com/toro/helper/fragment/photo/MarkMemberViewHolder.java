package com.toro.helper.fragment.photo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.toro.helper.R;
import com.toro.helper.modle.FamilyMemberInfo;
import com.toro.helper.modle.FamilyUserInfo;
import com.toro.helper.utils.ImageLoad;
import com.toro.helper.utils.StringUtils;
import com.toro.helper.view.RoundnessImageView;

/**
 * Create By liujia
 * on 2018/10/31.
 **/
public class MarkMemberViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private RoundnessImageView photo;
    private View rootView;
    private ImageView selectedIcon;

    public MarkMemberViewHolder(@NonNull View itemView) {
        super(itemView);
        photo = itemView.findViewById(R.id.head_img);
        name = itemView.findViewById(R.id.name_text);
        selectedIcon = itemView.findViewById(R.id.selected_icon);
        this.rootView = itemView;
    }

    public void init(int i, FamilyMemberInfo info, final View.OnClickListener listener,boolean isSelected) {
        String nameText = "";
        if(StringUtils.isNotEmpty(info.getRemarkName())){
            nameText = info.getRemarkName();
        }else {
            nameText = info.getUserInfo().getName();
        }
        name.setText(nameText);
        if(info.getUserInfo().getHeadPhoto() != null) {
            ImageLoad.GlidLoad(photo,info.getUserInfo().getHeadPhoto(),R.mipmap.default_head);
        } else {
            photo.setImageResource(R.mipmap.default_head);
        }
        if(isSelected) {
            selectedIcon.setVisibility(View.VISIBLE);
        } else {
            selectedIcon.setVisibility(View.GONE);
        }
        rootView.setTag(i);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onClick(v);
                }
            }
        });
    }
}
