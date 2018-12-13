package com.toro.helper.fragment;

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

import com.orhanobut.hawk.Hawk;
import com.toro.helper.R;
import com.toro.helper.app.App;
import com.toro.helper.base.BaseFragment;
import com.toro.helper.fragment.photo.PhotoAdapter;
import com.toro.helper.modle.data.FamilyPhotoData;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.modle.data.listener.FamilyPhotoDataOnChangedListener;
import com.toro.helper.utils.WifiUtil;
import com.toro.helper.view.AutoLoadRecyclerView;
import com.toro.helper.view.RecyclerItemDecoration;

import static com.toro.helper.app.AppConfig.KEY_WIFI_PHOTO;

/**
 * Create By liujia
 * on 2018/10/23.
 **/
public class FamilyPhotoFragment extends BaseFragment implements FamilyPhotoDataOnChangedListener {

    private AutoLoadRecyclerView recyclerView;
    private TextView emptyHint;
    private ProgressBar loadingProgress;
    private PhotoAdapter adapter;
    private FamilyPhotoData photoData;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =   inflater.inflate(
                R.layout.list_fragment, container, false
        );
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ToroDataModle.getInstance().addToroDataModeOnChangeListener(this);
        updatePhotoList();
    }

    @Override
    public void onPause() {
        super.onPause();
        ToroDataModle.getInstance().removeToroDataModeOnChangeListener(this);
    }

    private void initView(View rootView) {
        adapter = new PhotoAdapter(getContext(),null,false);
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
    }

    private void updatePhotoList() {
        if(Hawk.get(KEY_WIFI_PHOTO,false) && !WifiUtil.isConnectWifi(App.getInstance())){
            photoData = null;
            Toast.makeText(App.getInstance(),R.string.need_wifi_photo,Toast.LENGTH_SHORT).show();
        } else {
            photoData = ToroDataModle.getInstance().getFamilyPhotoData();
        }
        if(photoData == null || photoData.getPhotoDatas() == null || photoData.getPhotoDatas().size() < 1) {
            showEmptyHint();
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            loadingProgress.setVisibility(View.GONE);
            emptyHint.setVisibility(View.GONE);
            adapter.updatePhotoDatas(photoData.getPhotoDatas());
        }
        recyclerView.setLoadMoreListener(new AutoLoadRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {
                ToroDataModle.getInstance().updateMoreFamilyPhotoList();
            }
        });
    }

    private void showEmptyHint() {
        recyclerView.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.GONE);
        emptyHint.setVisibility(View.VISIBLE);
        emptyHint.setText(getString(R.string.empty_photo_hint));
    }


    @Override
    public void onChange(Object obj) {
        updatePhotoList();
    }
}
