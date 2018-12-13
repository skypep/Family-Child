package com.toro.helper.modle.data;

import com.toro.helper.modle.photo.PhotoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By liujia
 * on 2018/10/31.
 **/
public class FamilyPhotoData {
    private List<PhotoData> photoDatas;
    public int pageCount = 10;// 一页多少条数据

    public List<PhotoData> getPhotoDatas() {
        return photoDatas;
    }

    public void setPhotoDatas(List<PhotoData> datas) {
        this.photoDatas = datas;
    }

    /**
     * 新的分页机制决定了当最后一页不满时，重复刷新可能会产生垃圾数据，过滤下
     * @param datas
     */
    public void appendPhotoDatas(List<PhotoData> datas) {
        if(datas == null || datas.size() < 1) {
            return;
        }
        if(datas.size() == pageCount) {
            this.photoDatas.addAll(datas);
        } else {
            for(PhotoData newData:datas) {
                for(int i = 0;i < this.photoDatas.size(); i ++) {
                    PhotoData oldData = this.photoDatas.get(i);
                    if(newData.getId() == oldData.getId()) {
                        break;
                    }
                    if(i == this.photoDatas.size() - 1) {
                        this.photoDatas.add(newData);
                    }
                }
            }
        }

    }

//    public int getLimit() {
//        if(photoDatas == null || photoDatas.size() < 1){
//            return pageCount;
//        }else {
//            return photoDatas.size()>pageCount?photoDatas.size():pageCount;
//        }
//    }

    public int getMaxPageCount() {
        if(photoDatas == null || photoDatas.size() < 1){
            return pageCount;
        } else {
            int count = photoDatas.size()/pageCount; // 当前刷了几页
            if(photoDatas.size()%pageCount != 0) {
                count = count + 1;
            }
            return count * pageCount;
        }
    }

    public int getPageIndex() {
        if(photoDatas == null || photoDatas.size() < 1){
            return 1;
        } else {
            return (photoDatas.size() / pageCount) + 1;
        }
    }

//    public void setPhotoDatasAppend(List<PhotoData> datas) {
//        if(photoDatas == null || photoDatas.size() < 1) {
//            return;
//        }
//        if(photoDatas == null) {
//            photoDatas = new ArrayList<>();
//        }
//        if(photoDatas.size() > 0) {
//            for(PhotoData newData:datas) {
//                for(int i = 0;i < this.photoDatas.size(); i ++) {
//                    PhotoData oldData = this.photoDatas.get(i);
//                    if(newData.getId() == oldData.getId()) {
//                        break;
//                    }
//                    if(i == this.photoDatas.size() - 1) {
//                        this.photoDatas.add(newData);
//                    }
//                }
//            }
//        } else{
//            photoDatas.addAll(datas);
//        }
//    }
}
