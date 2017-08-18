/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.goods.entity;

import org.hibernate.validator.constraints.Length;

import java.util.List;
import com.google.common.collect.Lists;

import com.its.common.persistence.DataEntity;

/**
 * 商品信息Entity
 * 
 * @author test
 * @version 2017-07-04
 */
public class GoodsInfo extends DataEntity<GoodsInfo> {

    private static final long serialVersionUID = 1L;
    private String businessInfoId; // 商家id
    private String serialNumbers; // 商品编号
    private String name; // 商品名称
    private String sortInfoId; // 分类ID
    private String imgs; // 轮播图片
    private Double basePrice; // 原价
    private Double benefitPrice; // 优惠价
    private Integer stock; // 库存
    private String baseUnitId; // 商品单位
    private String isBase; // 服务单位是否来自公共字典表:0否1是
    private String content; // 图文详情
    private String quota; // 是否限购:0否1是
    private String quotaNum; // 限购数量
    private String recommend; // 是否推荐:0否1是
    private String state; // 上下架状态 0下架1上架
    private String[] checkedSkuValue; // 被选中的规格
    private String sortInfoName; // 分类名称
    private String sortInfoOrder; // 分类排序
    private String sortItem; // 排序项目
    private String sort; // 排序顺
    private List<String> imageUrls; // 一览用图片路径
    private List<GoodsInfoPic> picList; // 图片LIST
    private String delImageName; // 商品编辑时删除的图片名称
    private String warnNum; // 库存预警数量
    private List<GoodsSkuPrice> goodsSkuPriceList = Lists.newArrayList(); // 子表列表

    public GoodsInfo() {
        super();
    }

    public GoodsInfo(String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "商家id长度必须介于 1 和 64 之间")
    public String getBusinessInfoId() {
        return businessInfoId;
    }

    public void setBusinessInfoId(String businessInfoId) {
        this.businessInfoId = businessInfoId;
    }

    @Length(min = 1, max = 11, message = "商品编号长度必须介于 1 和 11 之间")
    public String getSerialNumbers() {
        return serialNumbers;
    }

    public void setSerialNumbers(String serialNumbers) {
        this.serialNumbers = serialNumbers;
    }

    @Length(min = 0, max = 64, message = "商品名称长度必须介于 0 和 64 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 1, max = 64, message = "分类ID长度必须介于 1 和 64 之间")
    public String getSortInfoId() {
        return sortInfoId;
    }

    public void setSortInfoId(String sortInfoId) {
        this.sortInfoId = sortInfoId;
    }

    @Length(min = 0, max = 512, message = "轮播图片长度必须介于 0 和 512 之间")
    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getBenefitPrice() {
        return benefitPrice;
    }

    public void setBenefitPrice(Double benefitPrice) {
        this.benefitPrice = benefitPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Length(min = 0, max = 64, message = "商品单位长度必须介于 0 和 64 之间")
    public String getBaseUnitId() {
        return baseUnitId;
    }

    public void setBaseUnitId(String baseUnitId) {
        this.baseUnitId = baseUnitId;
    }

    @Length(min = 0, max = 1, message = "服务单位是否来自公共字典表:0否1是长度必须介于 0 和 1 之间")
    public String getIsBase() {
        return isBase;
    }

    public void setIsBase(String isBase) {
        this.isBase = isBase;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Length(min = 0, max = 1, message = "是否限购:0否1是长度必须介于 0 和 1 之间")
    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public String getQuotaNum() {
        return quotaNum;
    }

    public void setQuotaNum(String quotaNum) {
        this.quotaNum = quotaNum;
    }

    @Length(min = 0, max = 1, message = "是否推荐:0否1是长度必须介于 0 和 1 之间")
    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<GoodsSkuPrice> getGoodsSkuPriceList() {
        return goodsSkuPriceList;
    }

    public void setGoodsSkuPriceList(List<GoodsSkuPrice> goodsSkuPriceList) {
        this.goodsSkuPriceList = goodsSkuPriceList;
    }

    public String getSortInfoName() {
        return sortInfoName;
    }

    public void setSortInfoName(String sortInfoName) {
        this.sortInfoName = sortInfoName;
    }

    public String[] getCheckedSkuValue() {
        return checkedSkuValue;
    }

    public void setCheckedSkuValue(String[] checkedSkuValue) {
        this.checkedSkuValue = checkedSkuValue;
    }

    public String getSortItem() {
        return sortItem;
    }

    public void setSortItem(String sortItem) {
        this.sortItem = sortItem;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSortInfoOrder() {
        return sortInfoOrder;
    }

    public void setSortInfoOrder(String sortInfoOrder) {
        this.sortInfoOrder = sortInfoOrder;
    }

    public List<GoodsInfoPic> getPicList() {
        return picList;
    }

    public void setPicList(List<GoodsInfoPic> picList) {
        this.picList = picList;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getDelImageName() {
        return delImageName;
    }

    public void setDelImageName(String delImageName) {
        this.delImageName = delImageName;
    }

    public String getWarnNum() {
        return warnNum;
    }

    public void setWarnNum(String warnNum) {
        this.warnNum = warnNum;
    }

}