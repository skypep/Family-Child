package com.toro.helper.modle.data;

import com.toro.helper.modle.FamilyMemberInfo;
import com.toro.helper.modle.photo.PhotoData;

import java.util.List;

/**
 * Create By liujia
 * on 2018/10/31.
 **/
public class FamilyMemberData {
    List<FamilyMemberInfo> familyMemberDatas;
    public int pageCount = 10;// 一页多少条数据

    public List<FamilyMemberInfo> getFamilyMemberDatas() {
        return familyMemberDatas;
    }

    public void setFamilyMemberDatas(List<FamilyMemberInfo> familyMemberDatas) {
        this.familyMemberDatas = familyMemberDatas;
    }

    public void appendPhotoDatas(List<FamilyMemberInfo> datas) {
        if(datas == null || datas.size() < 1) {
            return;
        }
        if(datas.size() == pageCount) {
            this.familyMemberDatas.addAll(datas);
        } else {
            for(FamilyMemberInfo newData:datas) {
                for(int i = 0;i < this.familyMemberDatas.size(); i ++) {
                    FamilyMemberInfo oldData = this.familyMemberDatas.get(i);
                    if(newData.getId() == oldData.getId()) {
                        break;
                    }
                    if(i == this.familyMemberDatas.size() - 1) {
                        this.familyMemberDatas.add(newData);
                    }
                }
            }
        }
    }

//    public int getLimit() {
//        if(familyMemberDatas == null || familyMemberDatas.size() < 1){
//            return pageCount;
//        }else {
//            return familyMemberDatas.size()>pageCount?familyMemberDatas.size():pageCount;
//        }
//    }

    public int getMaxPageCount() {
        if(familyMemberDatas == null || familyMemberDatas.size() < 1){
            return pageCount;
        } else {
            int count = familyMemberDatas.size()/pageCount; // 当前刷了几页
            if(familyMemberDatas.size()%pageCount != 0) {
                count = count + 1;
            }
            return count * pageCount;
        }
    }

    public int getPageIndex() {
        if(familyMemberDatas == null || familyMemberDatas.size() < 1){
            return 1;
        } else {
            return (familyMemberDatas.size() / pageCount) + 1;
        }
    }

    public FamilyMemberInfo getMemberInfoByid(int id) {
        try{
            for(FamilyMemberInfo info :familyMemberDatas){
                if(info.getId() == id) {
                    return info;
                }
            }
        }catch (Exception e){

        }

        return null;
    }

    public FamilyMemberInfo getMemberInfoByUid(int uid) {
        try{
            for(FamilyMemberInfo info :familyMemberDatas){
                if(info.getUserInfo().getUid() == uid) {
                    return info;
                }
            }
        }catch (Exception e){

        }

        return null;
    }
}
