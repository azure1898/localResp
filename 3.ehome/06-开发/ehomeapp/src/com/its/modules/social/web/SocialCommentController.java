/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.social.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.its.common.config.Global;
import com.its.common.web.BaseController;
import com.its.modules.social.bean.SocialCommentBean;
import com.its.modules.social.common.DateUtil;
import com.its.modules.social.common.SocialGlobal;
import com.its.modules.social.entity.SocialComment;
import com.its.modules.social.entity.SocialSpeak;
import com.its.modules.social.entity.SocialSubRelation;
import com.its.modules.social.entity.SocialSubject;
import com.its.modules.social.service.SocialCommentService;
import com.its.modules.social.service.SocialSpeakService;
import com.its.modules.social.service.SocialSubRelationService;
import com.its.modules.social.service.SocialSubjectService;
import com.its.modules.sys.utils.UserUtils;

import net.sf.json.JSONObject;

/**
 * @Description：评论手机端相关接口
 * @Author：刘浩浩
 * @Date：2017年8月4日
 */
@Controller
@RequestMapping(value = "${appPath}/comment")
public class SocialCommentController extends BaseController {

	@Autowired
	private SocialSubjectService socialSubjectService;

	@Autowired
	private SocialCommentService socialCommentService;
	
	@Autowired
	private SocialSpeakService socialSpeakService;
	
	@Autowired
	private SocialSubRelationService socialSubRelationService;
	
	/**
	 * @Description：详情页面评论列表
	 * @Author：刘浩浩
	 * @Date：2017年8月4日
	 * @param userId 当前用户ID
	 * @param speakId 发言ID
	 * @param pageIndex 分页页码(从0开始为起始页)
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="commentList",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getCommontList(String userId, String speakId, int pageIndex) throws Exception{
		//结果封装
		Map<String, Object> toJson = new HashMap<String, Object>();
		if(StringUtils.isEmpty(speakId)){
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "发言ID为空");
			String result = JSONObject.fromObject(toJson).toString();
			return result;
		}
		
		int pageSize = pageIndex==0 ? SocialGlobal.PAGE_SIZE_INDEX : SocialGlobal.PAGE_SIZE;
		pageIndex = pageIndex == 0 ? pageSize * pageIndex : pageSize * pageIndex -SocialGlobal.PAGE_SIZE_INDEX;
		SocialComment socialComment = new SocialComment();
		socialComment.setDelflag(SocialGlobal.COMMENT_DEL_FLAG_NO);
		socialComment.setSpeakid(speakId);
		List<SocialCommentBean> cmtList = socialCommentService.findCommentBeanList(userId, socialComment, pageIndex, pageSize);
		List<Map> dataList = new ArrayList();
		if(cmtList!=null && cmtList.size()>0){
			for(SocialCommentBean cb: cmtList){
				Map dataListMap = new HashMap();
				dataListMap.put("userId", cb.getUserid());
				dataListMap.put("userName", cb.getNickName());
				dataListMap.put("headPicSrc", cb.getPhoto());
				
				dataListMap.put("cmtId", cb.getId());
				String createTime = DateUtil.getDaysBeforeNow(cb.getCreatetime());
				dataListMap.put("createTime", createTime);
				dataListMap.put("content", cb.getContent());
				
				dataListMap.put("isPraise", cb.getIsPraise());//是否点赞
				dataListMap.put("countPraise", cb.getCountPraise());//赞的数量
				
				//根据ID查询话题集合
				List<SocialSubject> subjectList = socialSubjectService.findAllByfkId(cb.getId(), SocialGlobal.SUB_RELATION_CMT);
				List<Map> resultList = new ArrayList();
				Map<String, Object> subMap = new HashMap<String, Object>();
				if(subjectList!=null && subjectList.size()>0){
					for(SocialSubject sub : subjectList){
						subMap.put("subjectId", sub.getId());
						subMap.put("subName", sub.getSubname());
						resultList.add(subMap);
					}
				}
				dataListMap.put("subjectList", resultList);
				
				int secCountCmt = socialCommentService.getSecCmtCount(cb.getId());
				dataListMap.put("secCountCmt", secCountCmt);//二级评论数量
				
				dataList.add(dataListMap);
			}
		}
		
		toJson.put("userId", userId);
		toJson.put("speakId", speakId);
		toJson.put("pageIndex", pageIndex);
		toJson.put("data", dataList);
		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("message", "查看评论列表成功");
		
		String result = JSONObject.fromObject(toJson).toString();
		System.out.println("+++++++" + result);
		return result;
	}
	
	/**
	 * @Description：子评论列表
	 * @Author：王萌萌
	 * @Date：2017年8月7日
	 * @param commentId 评论ID
	 * @param userId 当前用户id
	 * @param pageIndex 分页页码(从0开始为起始页)
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="secCommentList",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getSecCommentList(String commentId, String userId, int pageIndex) {
		//结果封装
		Map<String, Object> toJson = new HashMap<String, Object>();
		//判断评论ID是否为空
		if(StringUtils.isEmpty(commentId)){
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "评论ID为空");
			String result = JSONObject.fromObject(toJson).toString();
			return result;
		}
		SocialComment socialComment = new SocialComment();
		socialComment.setId(commentId);
		socialComment.setDelflag(SocialGlobal.COMMENT_DEL_FLAG_NO);
		//根据commentId查询socialCommentBean
		SocialCommentBean socialCommentBean = socialCommentService.findCommentBean(socialComment);
		toJson.put("id", commentId);
		toJson.put("username", socialCommentBean.getNickName());
		toJson.put("headPicSrc", socialCommentBean.getPhoto());
		String createtime = DateUtil.getDaysBeforeNow(socialCommentBean.getCreatetime());
		toJson.put("createTime", createtime);
		toJson.put("countPraise", socialCommentBean.getCountPraise());
		toJson.put("content", socialCommentBean.getContent());
		int pageSize = pageIndex == 0 ? SocialGlobal.PAGE_SIZE_INDEX : SocialGlobal.PAGE_SIZE;
		pageIndex = pageIndex == 0 ? pageSize * pageIndex : pageSize * pageIndex -SocialGlobal.PAGE_SIZE_INDEX;
		//查询当前评论的子评论
		List<SocialCommentBean> scbList = socialCommentService.findSecCommentBeanList(userId, socialCommentBean, pageIndex, pageSize);
		List<Map> dataList = new ArrayList<Map>();
		if(scbList != null && scbList.size()>0) {
			//遍历子评论List
			for(SocialCommentBean scb : scbList) {
				Map dataListMap = new HashMap();
				dataListMap.put("id", scb.getId());
				dataListMap.put("userName", scb.getNickName());
				dataListMap.put("headPicSrc", scb.getPhoto());
				String createTime = DateUtil.getDaysBeforeNow(scb.getCreatetime());
				dataListMap.put("createTime", createTime);
				dataListMap.put("countPraise", scb.getCountPraise());
				dataListMap.put("content", scb.getContent());
				dataListMap.put("isPraise", scb.getIsPraise());
				
				dataList.add(dataListMap);
			}
		}
		
		toJson.put("userId", userId);
		toJson.put("commentId", commentId);
		toJson.put("pageIndex", pageIndex);
		toJson.put("data", dataList);
		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("message", "查看子评论列表成功");
		
		//将结果转换成String返回
		String result = JSONObject.fromObject(toJson).toString();
		System.out.println("+++++++" + result);
		return result;
	}
	
	/**
	 * @Description 子评论回复
	 * @author 王萌萌
	 * @Date 2017年8月7日
	 * @param pid 评论ID
	 * @param content 评论内容
	 * @param isForward 是否转发
	 * @param isComment 是否评论
	 * @return
	 */
	@RequestMapping(value="saveComment",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String saveComment(String pid, String content, String subjectIds, int isForward, int isComment) {
		//结果封装
				Map<String, Object> toJson = new HashMap<String, Object>();
				//判断pid是否为空
				if(StringUtils.isEmpty(pid)){
					toJson.put("code", Global.CODE_PROMOT);
					toJson.put("message", "评论ID为空");
					String result = JSONObject.fromObject(toJson).toString();
					return result;
				}
				//根据pid查询出要回复的评论
				SocialComment sc = new SocialComment();
				if(isComment == SocialGlobal.COMMENT_ISCOMMENT_NO) {
					SocialComment psc = socialCommentService.get(pid);
					sc.setFid(pid);
					sc.setSpeakid(psc.getSpeakid());
				} else {
					sc.setSpeakid(pid);
				}
				sc.setContent(content);
				sc.setIscomment(isComment);
				sc.setCreatetime(new Date());
				sc.setUserid(UserUtils.getUser().getId());
				sc.setDelflag(SocialGlobal.COMMENT_DEL_FLAG_NO);
				//保存回复
				socialCommentService.save(sc);
				if(!StringUtils.isEmpty(subjectIds)){
					String[] subjectIdArr = subjectIds.split(",");
					for(String subjectId : subjectIdArr) {
						SocialSubRelation ssr = new SocialSubRelation();
						ssr.setSubjectid(subjectId);
						ssr.setCommentid(sc.getId());
						socialSubRelationService.save(ssr);
					}
				}
				
				//如果用户选中回复并转发，那么再在转发表中添加转发相关信息
				if(SocialGlobal.COMMENT_AND_FORWARD_YES == isForward) {
					SocialSpeak ssf = new SocialSpeak();
					//获取要转发的发言
					SocialSpeak ss = socialSpeakService.get(sc.getSpeakid());
					
					ssf.setReason(content);
					ssf.setVisrange(SocialGlobal.FORWARD_VIS_RANGE_OPEN);
					ssf.setForbitcomment(SocialGlobal.FORWARD_FORBIT_COMMENT_NO);
					ssf.setForbidforward(SocialGlobal.FORWARD_FORBIT_FORWARD_NO);
					ssf.setUserid(UserUtils.getUser().getId());
					ssf.setCreatetime(new Date());
					ssf.setDelflag(SocialGlobal.SPEAK_DEL_FLAG_NO);
//					ssf.setVillageinfoid(ss.getVillageinfoid());
					ssf.setFid(ss.getId());
					if(StringUtils.isEmpty(ss.getRootid())) {
						ssf.setRootid(ss.getId());
					} else {
						ssf.setRootid(ss.getRootid());
					}
					socialSpeakService.save(ssf);
				}
				toJson.put("code", Global.CODE_SUCCESS);
				toJson.put("message", "评论成功");
				
				//将结果转换成String返回
				String result = JSONObject.fromObject(toJson).toString();
				System.out.println("+++++++" + result);
				return result;
	}
	
}