/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.operation.entity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.its.common.persistence.DataEntity;

/**
 * 团购管理Entity
 * @author caojing
 * @version 2017-06-28
 */
public class GroupPurchase extends DataEntity<GroupPurchase> {
	
	private static final long serialVersionUID = 1L;
	
	//private GroupPurchasetime groupPurchasetime; //团购时间
	private String groupId;		// 主表id
	private String moduleId;		// 模块ID
	private String villageInfoId;		// 楼盘ID
	private String businessinfoId;		// 商家ID
	private String groupPurcName;		// 团购名称
	private String groupPurcPic;		// 团购图片
	private Double marketMoney;		// 市场价
	private Double groupPurcMoney;		// 团购价
	private String restrictionNumber;		// 用户限购数
	private String supportType;		// 商家支持：0支持随时退  1支持过期退 2免预约
	private String groupPurcDetail;		// 团购详情
	private Date validityStartTime;		// 团购券有效期开始时间
	private Date validityEndTime;		// 团购券有效期结束时间
	private String useTime;		// 使用时间
	private String useRule;		// 使用规则
	private String sortNum;		// 排序序号
	private String groupPurcState;		// 团购状态：0待开始、1活动中、2已结束、3已撤消
	private String businessName;    //商家名称
	private String startTimes;   //团购开始日期
	private String endTimes;     //团购结束日期
	private String stockNums;    //团购库存量
	private String timeKey;      //团购开始时间下拉框取值用
	private String timeValue;    //团购开始时间下拉框取值用
	private String businessCategoryDictId;//商家下拉框取值用
	
	private boolean editFlag;    //团购活动可编辑标志
	private Date startTime;		// 团购开始时间
	private Date endTime;		// 团购结束时间
	private int stockNum;	// 库存量
	private List<String> userVillageIds;// 当前用户楼盘权限（楼盘信息ID）
	private List<GroupPurchasetime> groupPurchasetimeList = Lists.newArrayList();		// 子表列表
	private int inNum;	// 进行中团购时间count数
	
	public GroupPurchase() {
		super();
	}

	public GroupPurchase(String id){
		super(id);
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	
	@Length(min=0, max=64, message="模块ID长度必须介于 0 和 64 之间")
	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	
	@Length(min=0, max=64, message="商家ID长度必须介于 0 和 64 之间")
	public String getBusinessinfoId() {
		return businessinfoId;
	}

	public void setBusinessinfoId(String businessinfoId) {
		this.businessinfoId = businessinfoId;
	}
	
	@Length(min=0, max=200, message="团购名称长度必须介于 0 和 200 之间")
	public String getGroupPurcName() {
		return groupPurcName;
	}

	public void setGroupPurcName(String groupPurcName) {
		this.groupPurcName = groupPurcName;
	}
	
	@Length(min=0, max=200, message="团购图片长度必须介于 0 和 200 之间")
	public String getGroupPurcPic() {
		return groupPurcPic;
	}

	public void setGroupPurcPic(String groupPurcPic) {
		this.groupPurcPic = groupPurcPic;
	}
	
	public Double getMarketMoney() {
		return marketMoney;
	}

	public void setMarketMoney(Double marketMoney) {
		this.marketMoney = marketMoney;
	}
	
	public Double getGroupPurcMoney() {
		return groupPurcMoney;
	}

	public void setGroupPurcMoney(Double groupPurcMoney) {
		this.groupPurcMoney = groupPurcMoney;
	}
	
	@Length(min=0, max=11, message="用户限购数长度必须介于 0 和 11 之间")
	public String getRestrictionNumber() {
		return restrictionNumber;
	}

	public void setRestrictionNumber(String restrictionNumber) {
		this.restrictionNumber = restrictionNumber;
	}
	
	@Length(min=0, max=10, message="商家支持：0支持随时退  1支持过期退 2免预约长度必须介于 0 和 10 之间")
	public String getSupportType() {
		return supportType;
	}

	public void setSupportType(String supportType) {
		this.supportType = supportType;
	}
	
	public String getGroupPurcDetail() {
		return groupPurcDetail;
	}

	public void setGroupPurcDetail(String groupPurcDetail) {
		this.groupPurcDetail = groupPurcDetail;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getValidityStartTime() {
		return validityStartTime;
	}

	public void setValidityStartTime(Date validityStartTime) {
		this.validityStartTime = validityStartTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getValidityEndTime() {
		return validityEndTime;
	}

	public void setValidityEndTime(Date validityEndTime) {
		this.validityEndTime = validityEndTime;
	}
	
	@Length(min=0, max=200, message="使用时间长度必须介于 0 和 200 之间")
	public String getUseTime() {
		return useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	
	public String getUseRule() {
		return useRule;
	}

	public void setUseRule(String useRule) {
		this.useRule = useRule;
	}

	public String getSortNum() {
		return sortNum;
	}

	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}
	
	@Length(min=0, max=1, message="团购状态：0待开始、1活动中、2已结束、3已撤消长度必须介于 0 和 1 之间")
	public String getGroupPurcState() {
		return groupPurcState;
	}

	public void setGroupPurcState(String groupPurcState) {
		this.groupPurcState = groupPurcState;
	}
	
	public List<String> getSupportTypeList() {
		List<String> list = Lists.newArrayList();
		if (supportType != null){
			for (String s : StringUtils.split(supportType, ",")) {
				list.add(s);
			}
		}
		return list;
	}
	
	public void setSupportTypeList(List<String> list) {
		supportType = ","+StringUtils.join(list, ",")+",";
	}

	public String getStartTimes() {
		return startTimes;
	}

	public void setStartTimes(String startTimes) {
		this.startTimes = startTimes;
	}

	public String getEndTimes() {
		return endTimes;
	}

	public void setEndTimes(String endTimes) {
		this.endTimes = endTimes;
	}

	public String getStockNums() {
		return stockNums;
	}

	public void setStockNums(String stockNums) {
		this.stockNums = stockNums;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getTimeKey() {
		return timeKey;
	}

	public void setTimeKey(String timeKey) {
		this.timeKey = timeKey;
	}

	public String getTimeValue() {
		return timeValue;
	}

	public void setTimeValue(String timeValue) {
		this.timeValue = timeValue;
	}

	public String getBusinessCategoryDictId() {
		return businessCategoryDictId;
	}

	public void setBusinessCategoryDictId(String businessCategoryDictId) {
		this.businessCategoryDictId = businessCategoryDictId;
	}

	public boolean isEditFlag() {
		return editFlag;
	}

	public void setEditFlag(boolean editFlag) {
		this.editFlag = editFlag;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getStockNum() {
		return stockNum;
	}

	public void setStockNum(int stockNum) {
		this.stockNum = stockNum;
	}

	public String getVillageInfoId() {
		return villageInfoId;
	}

	public void setVillageInfoId(String villageInfoId) {
		this.villageInfoId = villageInfoId;
	}

	public List<String> getUserVillageIds() {
		return userVillageIds;
	}

	public void setUserVillageIds(List<String> userVillageIds) {
		this.userVillageIds = userVillageIds;
	}

	public List<GroupPurchasetime> getGroupPurchasetimeList() {
		return groupPurchasetimeList;
	}

	public void setGroupPurchasetimeList(
			List<GroupPurchasetime> groupPurchasetimeList) {
		this.groupPurchasetimeList = groupPurchasetimeList;
	}

	public int getInNum() {
		return inNum;
	}

	public void setInNum(int inNum) {
		this.inNum = inNum;
	}
	
}