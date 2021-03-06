/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.social.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.its.common.persistence.DataEntity;

/**
 * 话题管理Entity
 * @author wmm
 * @version 2017-07-31
 */
public class SocialSubject extends DataEntity<SocialSubject> {
	
	private static final long serialVersionUID = 1L;
	private String subname;		// 话题名称
	private int isrecommend;		// 是否推荐 1：是；0：否
	private String villageInfoId;   //楼盘信息id
	private String villageInfoName; //楼盘名称
	private String createrid;		// 创建人ID
	private String creatername;		// 创建人姓名
	private Date createtime;		// 创建时间
	private String ordernum;		// 顺序号
	private Date beginCreatetime;		// 开始 创建时间
	private Date endCreatetime;		// 结束 创建时间
	
	private String addrpro;  //所在省份
	private String addrcity;  //所在城市
	
	public SocialSubject() {
		super();
	}

	public SocialSubject(String id){
		super(id);
	}

	@Length(min=0, max=200, message="话题名称长度必须介于 0 和 200 之间")
	public String getSubname() {
		return subname;
	}

	public void setSubname(String subname) {
		this.subname = subname;
	}
	
	@Length(min=0, max=2, message="是否推荐 1：是；0：否长度必须介于 0 和 2 之间")
	public int getIsrecommend() {
		return isrecommend;
	}

	public void setIsrecommend(int isrecommend) {
		this.isrecommend = isrecommend;
	}
	
	@Length(min=0, max=64, message="楼盘信息id长度必须介于 0 和 64之间")
	public String getVillageInfoId() {
		return villageInfoId;
	}

	public void setVillageInfoId(String villageInfoId) {
		this.villageInfoId = villageInfoId;
	}

	@Length(min=0, max=128, message="楼盘名称长度必须介于 0 和 128之间")
	public String getVillageInfoName() {
		return villageInfoName;
	}

	public void setVillageInfoName(String villageInfoName) {
		this.villageInfoName = villageInfoName;
	}

	@Length(min=0, max=32, message="创建人ID长度必须介于 0 和 32 之间")
	public String getCreaterid() {
		return createrid;
	}

	public void setCreaterid(String createrid) {
		this.createrid = createrid;
	}
	
	@Length(min=0, max=50, message="创建人姓名长度必须介于 0 和 50 之间")
	public String getCreatername() {
		return creatername;
	}

	public void setCreatername(String creatername) {
		this.creatername = creatername;
	}
	
	@Length(min=0, max=16, message="创建时间长度必须介于 0 和 16 之间")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@Length(min=0, max=3, message="顺序号长度必须介于 0 和 3 之间")
	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	
	public Date getBeginCreatetime() {
		return beginCreatetime;
	}

	public void setBeginCreatetime(Date beginCreatetime) {
		this.beginCreatetime = beginCreatetime;
	}
	
	public Date getEndCreatetime() {
		return endCreatetime;
	}

	public void setEndCreatetime(Date endCreatetime) {
		this.endCreatetime = endCreatetime;
	}

	@Length(min=0, max=64, message="省份id长度必须介于 0 和 64之间")
	public String getAddrpro() {
		return addrpro;
	}

	public void setAddrpro(String addrpro) {
		this.addrpro = addrpro;
	}

	@Length(min=0, max=64, message="楼盘信息城市id长度必须介于 0 和 64之间")
	public String getAddrcity() {
		return addrcity;
	}

	public void setAddrcity(String addrcity) {
		this.addrcity = addrcity;
	}
		
}