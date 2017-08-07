/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.social.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.its.common.persistence.DataEntity;

/**
 * 消息提醒Entity
 * @author 刘浩浩
 * @version 2017-08-07
 */
public class SocialTips extends DataEntity<SocialTips> {
	
	private static final long serialVersionUID = 1L;
	private String userid;		// 用户id
	private String isnotice;		// 是否通知 1:已经通知 0 ：未通知
	private Date noticetime;		// 通知时间
	private String type;		// 提醒类型1：发言；2：评论 ；3： 转发。
	private String fkid;		// 外键id
	private String isread;		// 是否已读 1：已读 0：未读
	
	public SocialTips() {
		super();
	}

	public SocialTips(String id){
		super(id);
	}

	@Length(min=0, max=64, message="用户id长度必须介于 0 和 64 之间")
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	@Length(min=0, max=11, message="是否通知 1:已经通知 0 ：未通知长度必须介于 0 和 11 之间")
	public String getIsnotice() {
		return isnotice;
	}

	public void setIsnotice(String isnotice) {
		this.isnotice = isnotice;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getNoticetime() {
		return noticetime;
	}

	public void setNoticetime(Date noticetime) {
		this.noticetime = noticetime;
	}
	
	@Length(min=0, max=11, message="提醒类型1：发言；2：评论 ；3： 转发。长度必须介于 0 和 11 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=64, message="外键id长度必须介于 0 和 64 之间")
	public String getFkid() {
		return fkid;
	}

	public void setFkid(String fkid) {
		this.fkid = fkid;
	}
	
	@Length(min=0, max=11, message="是否已读 1：已读 0：未读长度必须介于 0 和 11 之间")
	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}
	
}