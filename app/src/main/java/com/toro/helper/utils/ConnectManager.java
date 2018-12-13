package com.toro.helper.utils;

import android.content.Context;
import android.util.JsonReader;

import com.toro.helper.modle.data.LocationInfo;
import com.toro.helper.modle.data.SafeguardInfo;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.modle.photo.PhotoItem;
import com.toro.helper.utils.okhttp.OkHttp;
import com.toro.helper.utils.okhttp.mutifile.listener.impl.UIProgressListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By liujia
 * on 2018/10/22.
 **/
public class ConnectManager {

    private static final int USER_TYPE = 2; // 0：管理员,1：老人机用户，2：子女端用户，3：未注册用户|

    public static final int GET_SCODE_FOR_LOGIN = 2;
    public static final int QUICK_LOGIN = 3;
    public static final int GET_SCODE_FOR_REGISTER = 4;
    public static final int REGISTER = 5;
    public static final int GET_NUMBER_CAPTCHAR = 6;
    public static final int LOGIN = 7;
    public static final int GET_SCODE_FOR_RESET_PWD = 8;
    public static final int RESET_PWD = 9;
    public static final int GET_PHOTO_LIST = 10;
    public static final int UPLOAD_PHOTO_LIST = 11;
    public static final int SUBMIT_PHOTO_LIST = 12;
    public static final int DOWNLOAD_IMAGE = 13;
    public static final int FAMILY_MENBER_LIST = 14;
    public static final int ADD_FAMILY_MENBER = 15;
    public static final int GET_LOGIN_USERE_INFO = 16;
    public static final int VERIFY_TOKEN = 17;
    public static final int SUBMIT_PERSIONAL_DETAILS = 18;
    public static final int REFRESH_TOKEN = 19;
    public static final int AGREEN_MEMBER = 20;
    public static final int GET_PHOTO_LIST_BY_UID = 21;
    public static final int DELETE_PHOTO_LIST = 22;
    public static final int GET_USER_PHONE_STATUS = 23;
    public static final int FIX_REMARK_NAME = 24;
    public static final int GET_MORE_PHOTO = 25;
    public static final int GET_MORE_MEMBER = 26;
    public static final int GET_HEALTH_DATA = 27;
    public static final int GET_TRAC_DATA = 28;
    public static final int DELETE_MEMBER_LIST = 29;
    public static final int GET_MORE_PHOTO_LIST_BY_UID = 30;
    public static final int GET_PRIVACY_POLICY = 31;
    public static final int GET_MEMBER_STATUS = 32;
    public static final int ACTIVE_MEMBER = 33;
    public static final int FEEDBACK = 34;
    public static final int SUBMIT_SAFEGUARD = 35;
    public static final int GET_SAFEGUARD = 36;

    private static final String mainUrl = "http://120.78.174.86:8888/";

    /**
     * 使用用户手机号获取短信验证码
     */
    private static final String scodeForLoginAction = "kinship-api/smsLoginValidateCode";

    /**
     * 使用手机号和短信验证码快速登陆
     */
    private static final String quickLoginAction = "kinship-api/quickLogin";

    /**
     * 使用用户手机号获取短信注册验证码
     */
    private static final String scodeForRegisterAction = "kinship-api/smsGetValidateCode";

    /**
     * 注册接口
     */
    private static final String registerAction = "kinship-api/register";

    /**
     * 获取纯数字验证码
     */
    private static final String getNumberCaptchar = "kinship-api/getValidateCode";

    /**
     * 登陆
     */
    private static final String loginAction = "kinship-api/login";

    /**
     * 使用用户手机号获取重置密码验证码
     */
    private static final String getScodeForResetPwdAction = "kinship-api/smsResetValidateCode";

    /**
     * 重置密码
     */
    private static final String resetPwdAction = "kinship-api/reset";

    /**
     * 获取照片列表
     */
    private static final String getPhotoListAction = "kinship-api/photograph/list";

    /**
     * 图片上传
     */
    private static final String uploadPhotoListAction = "kinship-api/upload/photos";

    /**
     * 发布照片
     */
    private static final String submitPhotoListAction = "kinship-api/photograph";

    /**
     * 下载图片
     */
    private static final String downloadImageAction = "kinship-api";

    /**
     * 家庭成员列表
     */
    private static final String familyMenberListAction = "kinship-api/member/list";

    /**
     * 增加家庭成员
     */
    private static final String addFamilyMenberAction = "kinship-api/member/add";

    /**
     * 获取登陆用户信息
     */
    private static final String getLoginUserInfo = "kinship-api/getLoginUser";

    /**
     * 验证token是否有效
     */
    private static final String verifyTokenAction = "kinship-api/verifyToken";

    /**
     * 修改用户信息
     */
    private static final String submitPersionalDaitelsAction = "kinship-api/user";

    /**
     * 刷新Token
     */
    private static final String refreshTokenAction = "kinship-api/refreshToken";

    /**
     * 接受成员邀请
     */
    private static final String agreenMemberAction = "kinship-api/member/status/";

    /**
     * 获取指定用户的照片
     */
    private static final String getPhotoListByUidAction = "kinship-api/photograph/getPhoto";

    /**
     * 批量删除照片
     */
    private static final String deletePhotoListAction = "kinship-api/photograph/batchDelete";

    /**
     * 获取uid对应的用户手机状态
     */
    private static final String getUserPhontStatusAction = "kinship-api/phoneStatus/";

    /**
     *  修改备注名
     */
    private static final String fixRemarkNameAction = "kinship-api/member/updateName";

    /**
     * 获取健康数据
     */
    private static final String getHealthDataAction = "kinship-api/health/getToday";

    /**
     * 获取轨迹
     */
    private static final String getTracDataAction = "http://39.108.131.154:8082/mobile-gps/getGpsList";

    /**
     * 删除家庭成员（双方删除）
     */
    private static final String deleteFamilyMemberAction = "kinship-api/member/batchDelete";

    /**
     * 用户协议
     */
    private static final String getPrivacyPolicyAction = "kinship-api/law";

    /**
     * 查看用户在不在线
     */
    private static final String getMemberStatusAction = "kinship-api/phoneStatus/getStatys";

    /**
     * 激活用户
     */
    private static final String activeMemberAction = "kinship-api/phoneStatus/activateUpload";

    /**
     * 反馈
     */
    private static final String feedbackAction = "kinship-api/feedbackAssistant";

    /**
     * 设置安全防护参数
     */
    private static final String submitSafeGuardAction = "kinship-api/securityFance";

    /**
     * 获取安全防护参数
     */
    private static final String getSafeGuardAction = "kinship-api/securityFance/getByFance";

    private static ConnectManager instance;

    public static ConnectManager getInstance() {
        if(instance == null) {
            instance = new ConnectManager();
        }
        return instance;
    }

    public boolean getScodeForLogin(OnHttpDataUpdateListener listener, String phoneNum) {
        try{
            JSONObject obj = new JSONObject();
            obj.put("phone",phoneNum);
            new NetWorkTask().execute(listener, GET_SCODE_FOR_LOGIN,mainUrl + scodeForLoginAction,obj);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean quickLogin(OnHttpDataUpdateListener listener, String phoneNum,String scode) {
        try{
            JSONObject obj = new JSONObject();
            obj.put("phone",phoneNum);
            obj.put("captcha",scode);
            obj.put("userType",USER_TYPE);
            new NetWorkTask().execute(listener,QUICK_LOGIN,mainUrl + quickLoginAction,obj);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean getScodeForRegister(OnHttpDataUpdateListener listener, String phoneNum) {
        try{
            JSONObject obj = new JSONObject();
            obj.put("phone",phoneNum);
            new NetWorkTask().execute(listener, GET_SCODE_FOR_REGISTER,mainUrl + scodeForRegisterAction,obj);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean register(OnHttpDataUpdateListener listener, String phoneNum,String scode,String password) {
        try{
            JSONObject obj = new JSONObject();
            obj.put("phone",phoneNum);
            obj.put("captcha",scode);
            obj.put("userType",USER_TYPE);
            obj.put("password",password);
            new NetWorkTask().execute(listener,REGISTER,mainUrl + registerAction,obj);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean getNumberCaptchar(OnHttpDataUpdateListener listener) {
        try{
            JSONObject obj = new JSONObject();
            new NetWorkTask().execute(listener,GET_NUMBER_CAPTCHAR,mainUrl + getNumberCaptchar,obj);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean login(OnHttpDataUpdateListener listener, String phoneNum,String pwd,String captcha){
        try{
            JSONObject obj = new JSONObject();
            obj.put("phone",phoneNum);
            obj.put("captcha",captcha);
            obj.put("password",pwd);
            obj.put("userType",USER_TYPE);
            new NetWorkTask().execute(listener,LOGIN,mainUrl + loginAction,obj);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean getScodeForResetPwd(OnHttpDataUpdateListener listener, String phoneNum) {
        try{
            JSONObject obj = new JSONObject();
            obj.put("phone",phoneNum);
            new NetWorkTask().execute(listener, GET_SCODE_FOR_RESET_PWD,mainUrl + getScodeForResetPwdAction,obj);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean resetPwd(OnHttpDataUpdateListener listener, String phoneNum,String pwd,String scode) {
        try{
            JSONObject obj = new JSONObject();
            obj.put("phone",phoneNum);
            obj.put("password",pwd);
            obj.put("captcha",scode);
            new NetWorkTask().execute(listener, RESET_PWD,mainUrl + resetPwdAction,obj);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean loadFamilyPhotoList(OnHttpDataUpdateListener listener,int offset,int limit,String token) {
        return updatePhotoList(listener,GET_PHOTO_LIST,offset,limit,token);
    }

    public boolean loadFamilyPhotoMore(OnHttpDataUpdateListener listener,int offset,int limit,String token) {
        return updatePhotoList(listener,GET_MORE_PHOTO,offset,limit,token);
    }

    private boolean updatePhotoList(OnHttpDataUpdateListener listener,int tag,int offset,int limit,String token) {
        try{
            JSONObject obj = new JSONObject();
            obj.put("pageIndex",offset);
            obj.put("pageSize",limit);
            new NetWorkTask().execute(listener, tag,mainUrl + getPhotoListAction,obj,token);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean uploadPhotos(Context context,OnHttpDataUpdateListener listener, ArrayList<String>photos, String token, UIProgressListener progressListener) {
        if(HttpUtils.useOkHttp) {
//            OkHttp.upLoadFile(UPLOAD_PHOTO_LIST,mainUrl + uploadPhotoListAction,photos.get(0),token,progressListener,listener);
            OkHttp.upLoadFiles(context,UPLOAD_PHOTO_LIST,mainUrl + uploadPhotoListAction,photos,token,progressListener,listener);
//            OkHttp.upLoadFile(UPLOAD_PHOTO_LIST,mainUrl + uploadPhotoListAction,photos,token,listener);
//            OkHttp.postFile(UPLOAD_PHOTO_LIST,mainUrl + uploadPhotoListAction,photos.get(0),token,listener);
            return true;
        } else {
            try{
                JSONObject obj = new JSONObject();
                new NetWorkTask().execute(listener, UPLOAD_PHOTO_LIST,mainUrl + uploadPhotoListAction,photos,token);
                return true;
            } catch (Exception e){

            }
            return false;
        }

    }

    public boolean uploadPhot(Context context,OnHttpDataUpdateListener listener, String photo, String token, UIProgressListener progressListener) {
        OkHttp.upLoadFile(context,UPLOAD_PHOTO_LIST,mainUrl + uploadPhotoListAction,photo,token,progressListener,listener);
        return true;
    }

    public boolean submitPhotoList(OnHttpDataUpdateListener listener, List<PhotoItem> fileUrls, List<Integer> uids, String message, int mode, String token) {
        try{
            JSONObject obj = new JSONObject();
            JSONArray photoArray = new JSONArray();
            for(PhotoItem item : fileUrls) {
                photoArray.put(item.json());
            }
            obj.put("fileUrls",photoArray);
            obj.put("message",message);
            obj.put("mode",mode);
            JSONArray uidArray = new JSONArray();
            for(int uid : uids) {
                uidArray.put(uid);
            }
            obj.put("uids",uidArray);
            new NetWorkTask().execute(listener, SUBMIT_PHOTO_LIST,mainUrl + submitPhotoListAction,obj,token);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public String getPhotoUrl(PhotoItem photo) {
        String url = mainUrl + downloadImageAction + "/" + photo.getFolder() + "/" + photo.getName();
        return url;
    }

    public boolean loadFamilyMemberList(OnHttpDataUpdateListener listener,int offset,int limit,String token) {
        return getFamilyMemberList(listener,FAMILY_MENBER_LIST,offset,limit,token);
    }

    public boolean loadMoreFamilyMemberList(OnHttpDataUpdateListener listener,int offset,int limit,String token) {
        return getFamilyMemberList(listener,GET_MORE_MEMBER,offset,limit,token);
    }

    public boolean getFamilyMemberList(OnHttpDataUpdateListener listener,int tag,int offset,int limit,String token) {
        try{
            JSONObject obj = new JSONObject();
            obj.put("pageIndex",offset);
            obj.put("pageSize",limit);
            new NetWorkTask().execute(listener, tag,mainUrl + familyMenberListAction,obj,token);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean addFamilyMember(OnHttpDataUpdateListener listener,String phone,String name,String token) {
        try{
            JSONObject obj = new JSONObject();

            JSONArray jArray = new JSONArray();
            JSONObject childObj = new JSONObject();
            childObj.put("phone",phone);
            childObj.put("remarkName",name);
            jArray.put(childObj);

            obj.put("memberList",jArray);

            new NetWorkTask().execute(listener, ADD_FAMILY_MENBER,mainUrl + addFamilyMenberAction,obj,token);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean getLoginUserInfo(OnHttpDataUpdateListener listener,String token) {
        JSONObject obj = new JSONObject();
        new NetWorkTask().execute(listener, GET_LOGIN_USERE_INFO,mainUrl + getLoginUserInfo,token);
        return true;
    }

    public boolean verifyTokenAction(OnHttpDataUpdateListener listener, String token) {
        try{
            JSONObject obj = new JSONObject();
            obj.put("token",token);
            new NetWorkTask().execute(listener,VERIFY_TOKEN,mainUrl + verifyTokenAction,obj);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean submitPersionalDaitels(OnHttpDataUpdateListener listener,int uid,String nickName,PhotoItem photo,String token) {
        try{
            JSONObject obj = new JSONObject();

            obj.put("uid",uid);
            obj.put("nickName",nickName);
            obj.put("headPhoto",photo.json());

            new NetWorkTask().execute(listener, SUBMIT_PERSIONAL_DETAILS,mainUrl + submitPersionalDaitelsAction,obj,token);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean submitPersionalDaitels(OnHttpDataUpdateListener listener,int uid,String nickName,String token) {
        try{
            JSONObject obj = new JSONObject();

            obj.put("uid",uid);
            obj.put("name",nickName);

            new NetWorkTask().execute(listener, SUBMIT_PERSIONAL_DETAILS,mainUrl + submitPersionalDaitelsAction,obj,token);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean refreshToken(OnHttpDataUpdateListener listener,String token) {
        JSONObject obj = new JSONObject();
        new NetWorkTask().execute(listener, REFRESH_TOKEN,mainUrl + refreshTokenAction,obj,token);
        return true;
    }

    public boolean agreenMember(OnHttpDataUpdateListener listener,int id,String token) {
        JSONObject obj = new JSONObject();
        new NetWorkTask().execute(listener, AGREEN_MEMBER,mainUrl + agreenMemberAction + id,obj,token);
        return true;
    }

    public boolean loadPhotoListByUid(OnHttpDataUpdateListener listener,int id,int offset,int limit,String token) {
        return getPhotoListByUid(listener,id,offset,limit,token,GET_PHOTO_LIST_BY_UID);
    }

    public boolean loadPhotoListByUidMore(OnHttpDataUpdateListener listener,int id,int offset,int limit,String token) {
        return getPhotoListByUid(listener,id,offset,limit,token,GET_MORE_PHOTO_LIST_BY_UID);
    }

    /**
     * 奇葩的访问方式，因为是get 所以offset和limit需要放到头里面.....so...ODUOKE
     * @param listener
     * @param id
     * @param token
     * @return
     */
    private boolean getPhotoListByUid(OnHttpDataUpdateListener listener,int id,int offset,int limit,String token,int tag){
        try{
            JSONObject obj = new JSONObject();
            obj.put("uid",id);
            obj.put("pageIndex",offset);
            obj.put("pageSize",limit);
            new NetWorkTask().execute(listener, tag,mainUrl + getPhotoListByUidAction,obj,token);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean deletePhotoList(OnHttpDataUpdateListener listener, List<Integer> uids, String token){
        try{
            JSONObject obj = new JSONObject();
            JSONArray uidArray = new JSONArray();
            for(int uid : uids) {
                uidArray.put(uid);
            }
            obj.put("ids",uidArray);
            new NetWorkTask().execute(listener, DELETE_PHOTO_LIST,mainUrl + deletePhotoListAction,obj,token);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean getUserPhoneStatus(OnHttpDataUpdateListener listener,int uid,String token) {
        new NetWorkTask().execute(listener, GET_USER_PHONE_STATUS,mainUrl + getUserPhontStatusAction + uid,token);
        return true;
    }

    public boolean fixRemarkName(OnHttpDataUpdateListener listener,int id,String remarkName,String token) {
        try{
            JSONObject obj = new JSONObject();
            obj.put("id",id);
            obj.put("remarkName",remarkName);
            new NetWorkTask().execute(listener, FIX_REMARK_NAME,mainUrl + fixRemarkNameAction,obj,token);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean getHealthData(OnHttpDataUpdateListener listener,int uid,String today,String token) {
        try{
            JSONObject obj = new JSONObject();
            obj.put("uid",uid);
            obj.put("today",today);
            new NetWorkTask().execute(listener, GET_HEALTH_DATA,mainUrl + getHealthDataAction,obj,token);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean getTracData(OnHttpDataUpdateListener listener,String sn) {
        try{
            JSONObject obj = new JSONObject();
            obj.put("sn",sn);
            new NetWorkTask().execute(listener, GET_TRAC_DATA,getTracDataAction,obj);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean deleteMemberList(OnHttpDataUpdateListener listener, List<Integer> uids, String token){
        try{
            JSONObject obj = new JSONObject();
            JSONArray uidArray = new JSONArray();
            for(int uid : uids) {
                uidArray.put(uid);
            }
            obj.put("ids",uidArray);
            new NetWorkTask().execute(listener, DELETE_MEMBER_LIST,mainUrl + deleteFamilyMemberAction,obj,token);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public void updatePrivacyPolicy(OnHttpDataUpdateListener listener) {
        new NetWorkTask().execute(listener, GET_PRIVACY_POLICY,mainUrl + getPrivacyPolicyAction);
    }

    public boolean getMemberStatus(OnHttpDataUpdateListener listener,String phone,String token) {
        try{
            JSONObject obj = new JSONObject();
            obj.put("phone",phone);
            new NetWorkTask().execute(listener, GET_MEMBER_STATUS,mainUrl + getMemberStatusAction,obj,token);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean activeMember(OnHttpDataUpdateListener listener,String phone,String token) {
        try{
            JSONObject obj = new JSONObject();
            obj.put("phone",phone);
            new NetWorkTask().execute(listener, ACTIVE_MEMBER,mainUrl + activeMemberAction,obj,token);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean feedback(OnHttpDataUpdateListener listener, String content){
        try{
            JSONObject obj = new JSONObject();
            obj.put("content",content);
            obj.put("contentType",1);
            obj.put("source",2);
            new NetWorkTask().execute(listener,FEEDBACK,mainUrl + feedbackAction,obj,ToroDataModle.getInstance().getLocalData().getToken());
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean submitSafeguard(OnHttpDataUpdateListener listener, String phoneNum, SafeguardInfo safeguardInfo, String token){
        try{
            JSONObject obj = new JSONObject();
            obj.put("parentsPhone",phoneNum);
            obj.put("longitude",safeguardInfo.getLocationInfo().getLatLng().longitude + "");
            obj.put("latitude",safeguardInfo.getLocationInfo().getLatLng().latitude + "");
            obj.put("scope",safeguardInfo.getScope());
            obj.put("position",safeguardInfo.getLocationInfo().getPoiTitle());
            new NetWorkTask().execute(listener,SUBMIT_SAFEGUARD,mainUrl + submitSafeGuardAction,obj,ToroDataModle.getInstance().getLocalData().getToken());
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean getSafeguard(OnHttpDataUpdateListener listener,String phoneNum,String token) {
        try{
            JSONObject obj = new JSONObject();
            obj.put("parentsPhone",phoneNum);
            new NetWorkTask().execute(listener,GET_SAFEGUARD,mainUrl + getSafeGuardAction,obj,ToroDataModle.getInstance().getLocalData().getToken());
            return true;
        } catch (Exception e){

        }
        return false;
    }
}
