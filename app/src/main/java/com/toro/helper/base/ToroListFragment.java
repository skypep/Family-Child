package com.toro.helper.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.toro.helper.R;
import com.toro.helper.modle.BaseResponeData;
import com.toro.helper.modle.DataModleParser;
import com.toro.helper.utils.OnHttpDataUpdateListener;
import com.toro.helper.utils.StringUtils;
import com.toro.helper.view.AutoLoadRecyclerView;
import com.toro.helper.view.RecyclerItemDecoration;

import java.util.List;

/**
 * Create By liujia
 * on 2018/10/19.
 **/
public abstract class ToroListFragment extends BaseFragment implements OnHttpDataUpdateListener {

    protected AutoLoadRecyclerView recyclerView;
    private TextView emptyHint;
    private ProgressBar loadingProgress;
    private ToroListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =   inflater.inflate(
                R.layout.list_fragment, container, false
        );
        initView(view);
        doanloadDatas(false);
        return view;
    }

    protected void initView(View rootView) {
        adapter = getAdapter();
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new RecyclerItemDecoration(getContext().getResources().getDimensionPixelOffset(R.dimen.photo_list_photo_offset)));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnPauseListenerParams(new AutoLoadRecyclerView.OnLoadImageSwitchListener() {
            @Override
            public void onLoadImageSwitch(boolean flag) {
                adapter.onLoadImageSwitch(flag);
            }
        },false,false);

        emptyHint = rootView.findViewById(R.id.empty_hint);
        loadingProgress = rootView.findViewById(R.id.loading_progress);
        setItemDecoration();
    }

    public void doanloadDatas(boolean isBackgroud){
        if(!isBackgroud) {
            showProgress();
        }
    }
    protected abstract ToroListAdapter getAdapter();
    protected abstract List<?> getDatas();
    protected abstract void updateDatas(String result);
    protected abstract String getEmptyHintString();
    protected abstract void setItemDecoration();


    protected void showEmptyHint() {
        recyclerView.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.GONE);
        emptyHint.setVisibility(View.VISIBLE);
        emptyHint.setText(getEmptyHintString());
    }

    private void showProgress() {
        recyclerView.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.VISIBLE);
        emptyHint.setVisibility(View.GONE);
    }

    protected void showPhotoList() {
        if(getDatas().size() < 1) {
            showEmptyHint();
        }else {
            recyclerView.setVisibility(View.VISIBLE);
            loadingProgress.setVisibility(View.GONE);
            emptyHint.setVisibility(View.GONE);
            adapter.updateDatas(getDatas());
        }
    }

    private void loadFailed() {
        showEmptyHint();
    }

    @Override
    public boolean bindData(int tag, Object object) {
        try{
            String result = (String) object;
            BaseResponeData data = DataModleParser.parserBaseResponeData(result);
            if(!data.isStatus()) {
                if(StringUtils.isEmpty(data.getMessage())) {
                    Toast.makeText(getContext(),getString(R.string.unknow_error),Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(getContext(),data.getMessage(),Toast.LENGTH_LONG).show();
                }
                loadFailed();
                return false;
            }
            updateDatas(data.getEntry());
            showPhotoList();
            return true;
        }catch (Exception e) {

        }
        loadFailed();
        return false;
    }
}
