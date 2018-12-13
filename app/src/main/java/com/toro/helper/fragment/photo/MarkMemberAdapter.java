package com.toro.helper.fragment.photo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toro.helper.R;
import com.toro.helper.modle.FamilyMemberInfo;

import java.util.List;

/**
 * Create By liujia
 * on 2018/10/31.
 **/
public class MarkMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FamilyMemberInfo> members;
    boolean[] markFlags;
    private View.OnClickListener onClickListener;

    public MarkMemberAdapter(List<FamilyMemberInfo> members) {
        this.members = members;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new MarkMemberViewHolder(inflater.inflate(R.layout.mark_member_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MarkMemberViewHolder holder = (MarkMemberViewHolder) viewHolder;
        holder.init(i,members.get(i),onClickListener,markFlags[i]);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public void updateDatas(List<FamilyMemberInfo> members,boolean[] markFlags) {
        this.members = members;
        this.markFlags = markFlags;
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.onClickListener = listener;
    }
}
