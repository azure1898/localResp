/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.module.entity;

import org.hibernate.validator.constraints.Length;

import com.its.common.persistence.DataEntity;

/**
 * 楼盘产品线专题推荐明细Entity
 * 
 * @author zhujiao
 * @version 2017-07-27
 */
public class VillageLinerecomspecialdetail extends DataEntity<VillageLinerecomspecialdetail> {

    private static final long serialVersionUID = 1L;
    private String villageLineRecomSpecialId; // 楼盘产品线专题推荐ID
    private String recomType; // 推荐类型：0商家 1模块
    private String sortNum; // 排序序号
    private String recomModuleId; // 推荐模块ID
    private String recomBusinessId; // 推荐商家ID
    private String businessCategoryDictId; // 商家分类ID
    private String describes; // 描述
    private String picUrl; // 图片
    private String picId; // 图片

    public VillageLinerecomspecialdetail() {
        super();
    }

    public VillageLinerecomspecialdetail(String id) {
        super(id);
    }

    @Length(min = 0, max = 64, message = "楼盘产品线专题推荐ID长度必须介于 0 和 64 之间")
    public String getVillageLineRecomSpecialId() {
        return villageLineRecomSpecialId;
    }

    public void setVillageLineRecomSpecialId(String villageLineRecomSpecialId) {
        this.villageLineRecomSpecialId = villageLineRecomSpecialId;
    }

    @Length(min = 0, max = 1, message = "推荐类型：0商家  1模块长度必须介于 0 和 1 之间")
    public String getRecomType() {
        return recomType;
    }

    public void setRecomType(String recomType) {
        this.recomType = recomType;
    }

    @Length(min = 0, max = 11, message = "排序序号长度必须介于 0 和 11 之间")
    public String getSortNum() {
        return sortNum;
    }

    public void setSortNum(String sortNum) {
        this.sortNum = sortNum;
    }

    public String getRecomModuleId() {
        return recomModuleId;
    }

    public void setRecomModuleId(String recomModuleId) {
        this.recomModuleId = recomModuleId;
    }

    public String getRecomBusinessId() {
        return recomBusinessId;
    }

    public void setRecomBusinessId(String recomBusinessId) {
        this.recomBusinessId = recomBusinessId;
    }

    public String getBusinessCategoryDictId() {
        return businessCategoryDictId;
    }

    public void setBusinessCategoryDictId(String businessCategoryDictId) {
        this.businessCategoryDictId = businessCategoryDictId;
    }

    @Length(min = 0, max = 64, message = "描述长度必须介于 0 和 64 之间")
    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    @Length(min = 0, max = 256, message = "图片长度必须介于 0 和 256 之间")
    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

}