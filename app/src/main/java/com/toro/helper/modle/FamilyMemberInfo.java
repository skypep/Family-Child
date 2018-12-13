package com.toro.helper.modle;

import com.toro.helper.R;
import com.toro.helper.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Create By liujia
 * on 2018/10/29.
 **/
public class FamilyMemberInfo implements Serializable {
    private int id;
    private String remarkName;
    private int status;
    private FamilyUserInfo userInfo;

    public static FamilyMemberInfo newInstance(JSONObject obj) throws JSONException {
        FamilyMemberInfo instance = new FamilyMemberInfo();
        instance.id = obj.getInt("id");
        instance.remarkName = obj.getString("remarkName");
        instance.status = obj.getInt("status");
        instance.userInfo = FamilyUserInfo.newInstance(obj.getString("user"));
        return instance;
    }

    public int getStatusStringRes() {
        switch (status) {
            case 0:
                return R.string.status_accepted;
            case 1:
                return R.string.status_wait_me_agree;
            case 2:
                return R.string.status_wait_other_agree;
                default:
                    return R.string.status_unknow;
        }
    }

    public String getDisplayName() {
        if(StringUtils.isNotEmpty(remarkName)) {
            return remarkName;
        }
        if(StringUtils.isNotEmpty(getUserInfo().getName())) {
            return getUserInfo().getName();
        }
        return getUserInfo().getPhone();
    }

    public int getId() {
        return id;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public int getStatus() {
        return status;
    }

    public FamilyUserInfo getUserInfo() {
        return userInfo;
    }
}
