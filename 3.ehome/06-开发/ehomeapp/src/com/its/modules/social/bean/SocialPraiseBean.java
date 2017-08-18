package com.its.modules.social.bean;

import com.its.modules.social.entity.SocialPraise;

/**
 * @Description：查询结果封装bean
 * @Author：刘浩浩
 * @Date：2017年8月4日
 */
public class SocialPraiseBean extends SocialPraise {

	private static final long serialVersionUID = 1L;
	
	private String nickName;//点赞人昵称
	
	private String photo;//点赞人头像

	private String speakId;//发言id

	private String images;//发言图片
	
	private String spkUserId;//发言人id

	private String spkUserName;//发言人用户名

	private String commentId;//评论id

	private int isComment;//是否评论  1：是 0 ：否
	
	private String cmtUserName;//评论人人用户名
	
	private String cmtUserId;//评论人id
	
	private String content;//内容

	private String noticeId;//公告id
	
	private String title;//公告标题
	
	private String summary;//公告摘要

	public String getSpkUserId() {
		return spkUserId;
	}

	public void setSpkUserId(String spkUserId) {
		this.spkUserId = spkUserId;
	}

	public String getSpkUserName() {
		return spkUserName;
	}

	public void setSpkUserName(String spkUserName) {
		this.spkUserName = spkUserName;
	}

	public String getCmtUserName() {
		return cmtUserName;
	}

	public void setCmtUserName(String cmtUserName) {
		this.cmtUserName = cmtUserName;
	}

	public String getCmtUserId() {
		return cmtUserId;
	}

	public void setCmtUserId(String cmtUserId) {
		this.cmtUserId = cmtUserId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	public String getSpeakId() {
		return speakId;
	}

	public void setSpeakId(String speakId) {
		this.speakId = speakId;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public int getIsComment() {
		return isComment;
	}

	public void setIsComment(int isComment) {
		this.isComment = isComment;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	
}