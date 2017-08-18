/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.social.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.its.common.persistence.DataEntity;

/**
 * 发言管理Entity
 * @author 刘浩浩
 * @version 2017-08-16
 */
public class SocialSpeak extends DataEntity<SocialSpeak> {
	
	private static final long serialVersionUID = 1L;
	private String userid;		// 发言人，用户ID
	private String tag;		// 标签
	private String content;		// 发言内容
	private int visrange;		// 可见范围 1：公开 2：好友可见； 3：粉丝可见
	private int forbitcomment;		// 是否禁止评论 1：是；0：否
	private int forbidforward;		// 是否禁止转发 1：是； 0：否
	private Date createtime;		// 发布时间
	private int istop;		// 是否置顶 0：否； 1：是
	private Date toptime;		// 置顶时间
	private String readnum;		// 阅读次数
	private String plateid;		// 发言所属板块 sys_plate.id
	private String villageinfoid;		// 楼盘id village_info.id
	private String images;		// 图片
	private int delflag;		// 数据状态0：已删除 1：正常
	private int isspeak;		// 发言标识1：发言；0：转发
	private String reason;		// 转发原因
	private String fid;		// 转发父id
	private String rootid;		// 转发原发言id
	private String noticeid;		// 公告ID
	private String title;		// 标题
	private String summary;		// 摘要
	private String remarks;     //备注
	
	public SocialSpeak() {
		super();
	}

	public SocialSpeak(String id){
		super(id);
	}

	@Length(min=0, max=64, message="发言人，用户ID长度必须介于 0 和 64 之间")
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	@Length(min=0, max=200, message="标签长度必须介于 0 和 200 之间")
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=11, message="可见范围 1：公开 2：好友可见； 3：粉丝可见长度必须介于 0 和 11 之间")
	public int getVisrange() {
		return visrange;
	}

	public void setVisrange(int visrange) {
		this.visrange = visrange;
	}
	
	@Length(min=0, max=11, message="是否禁止评论 1：是；0：否长度必须介于 0 和 11 之间")
	public int getForbitcomment() {
		return forbitcomment;
	}

	public void setForbitcomment(int forbitcomment) {
		this.forbitcomment = forbitcomment;
	}
	
	@Length(min=0, max=11, message="是否禁止转发 1：是； 0：否长度必须介于 0 和 11 之间")
	public int getForbidforward() {
		return forbidforward;
	}

	public void setForbidforward(int forbidforward) {
		this.forbidforward = forbidforward;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@Length(min=0, max=11, message="是否置顶 0：否； 1：是长度必须介于 0 和 11 之间")
	public int getIstop() {
		return istop;
	}

	public void setIstop(int istop) {
		this.istop = istop;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getToptime() {
		return toptime;
	}

	public void setToptime(Date toptime) {
		this.toptime = toptime;
	}
	
	@Length(min=0, max=11, message="阅读次数长度必须介于 0 和 11 之间")
	public String getReadnum() {
		return readnum;
	}

	public void setReadnum(String readnum) {
		this.readnum = readnum;
	}
	
	@Length(min=0, max=11, message="id长度必须介于 0 和 64 之间")
	public String getPlateid() {
		return plateid;
	}

	public void setPlateid(String plateid) {
		this.plateid = plateid;
	}
	
	@Length(min=0, max=11, message="id长度必须介于 0 和 64 之间")
	public String getVillageinfoid() {
		return villageinfoid;
	}

	public void setVillageinfoid(String villageinfoid) {
		this.villageinfoid = villageinfoid;
	}
	
	@Length(min=0, max=512, message="图片长度必须介于 0 和 512 之间")
	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}
	
	@Length(min=0, max=11, message="数据状态0：已删除 1：正常长度必须介于 0 和 11 之间")
	public int getDelflag() {
		return delflag;
	}

	public void setDelflag(int delflag) {
		this.delflag = delflag;
	}
	
	@Length(min=0, max=11, message="发言标识1：发言；0：转发长度必须介于 0 和 11 之间")
	public int getIsspeak() {
		return isspeak;
	}

	public void setIsspeak(int isspeak) {
		this.isspeak = isspeak;
	}
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Length(min=0, max=64, message="转发父id长度必须介于 0 和 64 之间")
	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}
	
	@Length(min=0, max=64, message="转发原发言id长度必须介于 0 和 64 之间")
	public String getRootid() {
		return rootid;
	}

	public void setRootid(String rootid) {
		this.rootid = rootid;
	}
	
	@Length(min=0, max=64, message="公告ID长度必须介于 0 和 64 之间")
	public String getNoticeid() {
		return noticeid;
	}

	public void setNoticeid(String noticeid) {
		this.noticeid = noticeid;
	}
	
	@Length(min=0, max=64, message="标题长度必须介于 0 和 64 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=200, message="摘要长度必须介于 0 和 200 之间")
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Length(min=0, max=255, message="备注长度必须介于 0 和 255 之间")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}