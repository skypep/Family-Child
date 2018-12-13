package com.toro.helper.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.toro.helper.R;
import com.toro.helper.activity.EditPersonalDetailsActivity;
import com.toro.helper.activity.FamilyMemberManagerActivity;
import com.toro.helper.activity.MainActivity;
import com.toro.helper.activity.MyPhotoActivity;
import com.toro.helper.activity.SettingActivity;
import com.toro.helper.base.BaseFragment;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.modle.data.ToroLoginUserData;
import com.toro.helper.modle.data.listener.LoginUserDataOnChangeListener;
import com.toro.helper.utils.ImageLoad;
import com.toro.helper.utils.StringUtils;
import com.toro.helper.view.RoundnessImageView;

import java.util.List;

/**
 * Create By liujia
 * on 2018/10/27.
 **/
public class MineFragment extends BaseFragment implements View.OnClickListener,LoginUserDataOnChangeListener {

    private RoundnessImageView headImageView;
    private ImageView headEditImage;
    private TextView nameText,phoneText;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =   inflater.inflate(
                R.layout.mine_fragment, container, false
        );
        rootView = view;
        updateView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupViewByLoginData();
        ToroDataModle.getInstance().addToroDataModeOnChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ToroDataModle.getInstance().removeToroDataModeOnChangeListener(this);
    }

    private void updateView() {
        headImageView = rootView.findViewById(R.id.head_img);
        headEditImage = rootView.findViewById(R.id.edit_image);
        nameText = rootView.findViewById(R.id.name_text);
        phoneText = rootView.findViewById(R.id.phone_text);

        View setting1 = rootView.findViewById(R.id.setting_item1);
        View setting2 = rootView.findViewById(R.id.setting_item2);
        View setting3 = rootView.findViewById(R.id.setting_item3);
        setting3.setVisibility(View.GONE);
        View setting4 = rootView.findViewById(R.id.setting_item4);

        setItemView(setting1,R.string.family_member_manager,R.mipmap.family_member_manager_icon,null);
        setItemView(setting2,R.string.my_family_photo,R.mipmap.my_photo_icon,null);
        setItemView(setting3,R.string.sms_mode,R.mipmap.sms_mode_icon,null);
        setItemView(setting4,R.string.setting,R.mipmap.setting_icon,null);

        headEditImage.setOnClickListener(this);
        setting1.setOnClickListener(this);
        setting2.setOnClickListener(this);
        setting3.setOnClickListener(this);
        setting4.setOnClickListener(this);
    }

    private void setupViewByLoginData() {
        ToroLoginUserData loginUserData = ToroDataModle.getInstance().getLoginUserData();
        if(loginUserData != null) {
            if(loginUserData.getHeadPhoto() != null) {
                ImageLoad.GlidLoad(headImageView,loginUserData.getHeadPhoto(),R.mipmap.default_head);
            } else {
                headImageView.setImageResource(R.mipmap.default_head);
            }
            if(StringUtils.isNotEmpty(loginUserData.getNickName())) {
                nameText.setText(loginUserData.getNickName());
            } else {
                nameText.setText(R.string.have_no_nick_name);
            }
            phoneText.setText(loginUserData.getPhone());
        }
    }

    private void setItemView(View itemView, int stringID, int imgID, View.OnClickListener listener) {
        ((ImageView)itemView.findViewById(R.id.title_icon)).setImageResource(imgID);
        ((TextView)itemView.findViewById(R.id.title_text)).setText(stringID);
        itemView.setOnClickListener(listener);
    }

    public void update() {
        updateView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_image:
                getActivity().startActivity(EditPersonalDetailsActivity.createIntent(getContext()));
                break;
            case R.id.setting_item1:
                startActivity(FamilyMemberManagerActivity.createIntent(getContext()));
                break;
            case R.id.setting_item2:
                if(ToroDataModle.getInstance().getLoginUserData()!= null) {
                    startActivity(MyPhotoActivity.createIntent(getContext(),ToroDataModle.getInstance().getLoginUserData().getUid()));
                }
                break;
            case R.id.setting_item3:
                break;
            case R.id.setting_item4:
                startActivity(SettingActivity.createIntent(getContext()));
                break;
        }
    }

    @Override
    public void onChange(Object obj) {
        setupViewByLoginData();
    }
}
