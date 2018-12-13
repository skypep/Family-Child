package com.toro.helper.fragment.family;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.toro.helper.R;
import com.toro.helper.modle.FamilyMemberInfo;
import com.toro.helper.utils.ImageLoad;
import com.toro.helper.view.RoundnessImageView;

/**
 * Create By liujia
 * on 2018/10/29.
 **/
public class FamilyMemberViewHolder extends RecyclerView.ViewHolder {

    private View rootView;
    private RoundnessImageView headImageView;
    private TextView nameTextView;
    private TextView statusTextView;
    private TextView agreenBt;
    private LinearLayout ListRightLayout;
    private ImageView selectAction;

    public FamilyMemberViewHolder(@NonNull View itemView, View.OnClickListener listener) {
        super(itemView);
        this.rootView = itemView;
        this.headImageView = rootView.findViewById(R.id.head_img);
        this.nameTextView = rootView.findViewById(R.id.text_name);
        this.statusTextView = rootView.findViewById(R.id.text_status);
        this.ListRightLayout = rootView.findViewById(R.id.list_right_layout);
        selectAction = itemView.findViewById(R.id.action_selecte);
        agreenBt = rootView.findViewById(R.id.agreen_bt);
        rootView.setOnClickListener(listener);
    }

    public void init(boolean needLoad, FamilyMemberInfo data, final View.OnClickListener agreenListener, int index) {
        selectAction.setVisibility(View.GONE);
        rootView.setTag(index);
        if(needLoad) {
            ImageLoad.GlidLoad(headImageView,data.getUserInfo().getHeadPhoto(),R.mipmap.default_head);
        }else {
            headImageView.setImageResource(R.mipmap.default_head);
        }
        nameTextView.setText(data.getDisplayName());

        if(data.getStatus() == 1) {
            agreenBt.setVisibility(View.VISIBLE);
            statusTextView.setVisibility(View.GONE);
            agreenBt.setTag(index);
            agreenBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agreenBt.setEnabled(false);
                    if(agreenListener != null) {
                        agreenListener.onClick(v);
                    }
                }
            });
        } else {
            agreenBt.setVisibility(View.GONE);
            statusTextView.setVisibility(View.VISIBLE);
            statusTextView.setText(data.getStatusStringRes());
        }
        ListRightLayout.setVisibility(View.VISIBLE);
    }

    public void initEditMode(int index,boolean isSelected,View.OnClickListener listener) {
        ListRightLayout.setVisibility(View.GONE);
        selectAction.setVisibility(View.VISIBLE);
        if(isSelected) {
            selectAction.setImageResource(R.mipmap.check_checked);
        } else {
            selectAction.setImageResource(R.mipmap.check_un_check);
        }
        selectAction.setTag(index);
        selectAction.setOnClickListener(listener);
    }
}
