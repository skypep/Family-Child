package com.toro.helper.modle.data;

import android.content.Context;

import com.toro.helper.app.App;
import com.toro.helper.modle.BaseResponeData;
import com.toro.helper.modle.DataModleParser;
import com.toro.helper.modle.data.listener.FamilyMemberDataOnChangeListener;
import com.toro.helper.modle.data.listener.FamilyPhotoDataOnChangedListener;
import com.toro.helper.modle.data.listener.LoginUserDataOnChangeListener;
import com.toro.helper.modle.data.listener.ToroDataModeOnChangeListener;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.utils.OnHttpDataUpdateListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By liujia
 * on 2018/10/31.
 **/
public class ToroDataModle {
    private static ToroDataModle instance;

    private List<ToroDataModeOnChangeListener> toroDataModeOnChangeListeners;

    private ToroLoginUserData loginUserData;
    private ToroLocalData localData;
    private FamilyPhotoData familyPhotoData;
    private FamilyMemberData familyMemberData;

    public static ToroDataModle getInstance() {
        if(instance == null) {
            instance = new ToroDataModle();
        }
        return instance;
    }

    public void init(Context context) {
        localData = new ToroLocalData(context);
    }

    private ToroDataModle() {
        toroDataModeOnChangeListeners = new ArrayList<>();
        familyPhotoData = new FamilyPhotoData();
        familyMemberData = new FamilyMemberData();
    }

    public void loginOut() {
        localData.setToken("");
        App.getInstance().RongYunDisConnect();
    }

    public void addToroDataModeOnChangeListener(ToroDataModeOnChangeListener listener) {
        toroDataModeOnChangeListeners.add(listener);
    }

    public void removeToroDataModeOnChangeListener(ToroDataModeOnChangeListener listener) {
        toroDataModeOnChangeListeners.remove(listener);
    }

    public void updateToroLoginUserData() {
        ConnectManager.getInstance().getLoginUserInfo(httpDataUpdateListener,localData.getToken());
    }

    public void updateToroFamilyPhotoList() {
        ConnectManager.getInstance().loadFamilyPhotoList(httpDataUpdateListener,1,familyPhotoData.getMaxPageCount(), getLocalData().getToken());
    }

    public void updateMoreFamilyPhotoList() {
        ConnectManager.getInstance().loadFamilyPhotoMore(httpDataUpdateListener,familyPhotoData.getPageIndex(),familyPhotoData.pageCount, getLocalData().getToken());
    }


    public void updateToroFamilyMemberList() {
        ConnectManager.getInstance().loadFamilyMemberList(httpDataUpdateListener,1,familyMemberData.getMaxPageCount(), getLocalData().getToken());
    }

    public void updateMoreFamilyMemberList() {
        ConnectManager.getInstance().loadMoreFamilyMemberList(httpDataUpdateListener,familyMemberData.getPageIndex(),familyMemberData.pageCount, getLocalData().getToken());
    }

    public void chnageLoginUserData(String result) {
        loginUserData = DataModleParser.parserLoginUserData(result);
        for(ToroDataModeOnChangeListener listener : toroDataModeOnChangeListeners) {
            if(listener instanceof LoginUserDataOnChangeListener) {
                listener.onChange(loginUserData);
            }
        }
    }

    private void addFamilyPhoto(String result) {
        familyPhotoData.appendPhotoDatas(DataModleParser.parserPhotoDatas(result));
        for(ToroDataModeOnChangeListener listener : toroDataModeOnChangeListeners) {
            if(listener instanceof FamilyPhotoDataOnChangedListener) {
                listener.onChange(familyPhotoData);
            }
        }
    }

    private void chnageFamilyPhoto(String result) {
        familyPhotoData.setPhotoDatas(DataModleParser.parserPhotoDatas(result));
        for(ToroDataModeOnChangeListener listener : toroDataModeOnChangeListeners) {
            if(listener instanceof FamilyPhotoDataOnChangedListener) {
                listener.onChange(familyPhotoData);
            }
        }
    }

    private void chnageFamilyMember(String result) {
        familyMemberData.setFamilyMemberDatas(DataModleParser.parserFamilyMemberDatas(result));
        for(ToroDataModeOnChangeListener listener : toroDataModeOnChangeListeners) {
            if(listener instanceof FamilyMemberDataOnChangeListener) {
                listener.onChange(familyPhotoData);
            }
        }
    }

    private void addFamilyMember(String result) {
        familyMemberData.appendPhotoDatas(DataModleParser.parserFamilyMemberDatas(result));
        for(ToroDataModeOnChangeListener listener : toroDataModeOnChangeListeners) {
            if(listener instanceof FamilyMemberDataOnChangeListener) {
                listener.onChange(familyPhotoData);
            }
        }
    }

    private OnHttpDataUpdateListener httpDataUpdateListener = new OnHttpDataUpdateListener() {
        @Override
        public boolean bindData(int tag, Object object) {
            try{
                String result = (String) object;
                BaseResponeData data = DataModleParser.parserBaseResponeData(result);
                if(data.isStatus()) {
                    switch (tag) {
                        case ConnectManager.GET_LOGIN_USERE_INFO:
                            chnageLoginUserData(data.getEntry());
                            return true;
                        case ConnectManager.GET_PHOTO_LIST:
                            chnageFamilyPhoto(data.getEntry());
                            return true;
                        case ConnectManager.FAMILY_MENBER_LIST:
                            chnageFamilyMember(data.getEntry());
                            return true;
                        case ConnectManager.GET_MORE_PHOTO:
                            addFamilyPhoto(data.getEntry());
                            return true;
                        case ConnectManager.GET_MORE_MEMBER:
                            addFamilyMember(data.getEntry());
                            return true;
                    }
                }
            }catch (Exception e) {

            }
            return false;
        }
    };

    public ToroLoginUserData getLoginUserData() {
        return loginUserData;
    }

    public ToroLocalData getLocalData() {
        return localData;
    }

    public FamilyPhotoData getFamilyPhotoData() {
        return familyPhotoData;
    }

    public FamilyMemberData getFamilyMemberData() {
        return familyMemberData;
    }
}
