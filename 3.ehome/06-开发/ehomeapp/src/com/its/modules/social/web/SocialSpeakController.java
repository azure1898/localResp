/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.social.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.its.common.config.Global;
import com.its.common.web.BaseController;
import com.its.modules.social.bean.SocialCommentBean;
import com.its.modules.social.bean.SocialForwardBean;
import com.its.modules.social.bean.SocialSpeakBean;
import com.its.modules.social.common.DateUtil;
import com.its.modules.social.common.SocialGlobal;
import com.its.modules.social.entity.SocialComment;
import com.its.modules.social.entity.SocialForward;
import com.its.modules.social.entity.SocialSpeak;
import com.its.modules.social.entity.SocialSubject;
import com.its.modules.social.service.SocialCommentService;
import com.its.modules.social.service.SocialForwardService;
import com.its.modules.social.service.SocialSpeakService;
import com.its.modules.social.service.SocialSubjectService;

import net.sf.json.JSONObject;

/**
 * @Description：发言手机端相关接口
 * @Author：刘浩浩
 * @Date：2017年8月4日
 */
@Controller
@RequestMapping(value = "${appPath}/speak")
public class SocialSpeakController extends BaseController {

	@Autowired
	private SocialSpeakService socialSpeakService;
	
	@Autowired
	private SocialForwardService socialForwardService;
	
	@Autowired
	private SocialSubjectService socialSubjectService;

	@Autowired
	private SocialCommentService socialCommentService;
	
	/**
	 * @Description：首页发言列表
	 * @Author：刘浩浩
	 * @Date：2017年8月4日
	 * @param userId 用户id
	 * @param isAll 是否查询全部
	 * @param pageIndex  分页页码(从0开始为起始页)
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="speakList",method = {RequestMethod.POST,RequestMethod.GET})
	public String getSpeakList(String userId, int isAll, int pageIndex) throws Exception{
		
		//结果封装
		Map<String, Object> toJson = new HashMap<String, Object>();
		
		String villageInfoId = "1";
		int pageSize = SocialGlobal.SPEAK_PAGE_SIZE;
		
		List<SocialSpeakBean> socialSpeakBeanList = new ArrayList();
		SocialSpeak socialSpeak = new SocialSpeak();
		socialSpeak.setVillageinfoid(villageInfoId);
		socialSpeak.setDelflag(SocialGlobal.SPEAK_DEL_FLAG_NO);
		if(isAll==SocialGlobal.SPEAK_LIST_ALL){//全部
			socialSpeakBeanList = socialSpeakService.findListByUserId(null, socialSpeak, pageSize * pageIndex, pageSize);
		}else{//只看我关注的
			socialSpeakBeanList = socialSpeakService.findListByUserId(userId, socialSpeak, pageSize * pageIndex, pageSize);
		}
		
		List<Map> resultList = new ArrayList(); 
		if(socialSpeakBeanList!=null && socialSpeakBeanList.size()>0){
			for(SocialSpeakBean spb : socialSpeakBeanList){
				Map<String, Object> data = new HashMap<String, Object>();
				//人员属性
				data.put("userName", spb.getNickName());//发言人姓名 昵称
				data.put("headPicSrc", spb.getPhoto());//发言人头像
				
				//发言属性
				data.put("speakId", spb.getId());//发言id
				data.put("speakUserId", spb.getUserid());//发言人id
				String createTime = DateUtil.getDaysBeforeNow(spb.getCreatetime());
				data.put("createTime", createTime);//发言时间
				data.put("content", spb.getContent());//发言内容
				
				//根据ID 获取话题集合
				String speakId = spb.getId();
				List<SocialSubject> subjectList = socialSubjectService.findAllByfkId(speakId, SocialGlobal.SUB_RELATION_SPK);
				Map<String, Object> subMap = new HashMap<String, Object>();
				if(subjectList!=null && subjectList.size()>0){
					for(SocialSubject sub : subjectList){
						subMap.put("subjectId", sub.getId());
						subMap.put("subName", sub.getSubname());
					}
					
				}
				data.put("subjectList", subMap);
				
				//获取发言图片集合
				data.put("imgsList", spb.getImages());
				
				//发言相关属性
				data.put("isFocus", spb.getIsFocus());//是否对发言人关注
				data.put("isPraise", spb.getIsPraise());//是否点赞
				data.put("countForward", spb.getCountForward());//转发数量
				data.put("countComment", spb.getCountComment());//评论数量
				data.put("countPraise", spb.getCountPraise());//点赞数量
				resultList.add(data);
			}
			
		}
		
		
		toJson.put("userId", userId);
		toJson.put("pageIndex", pageIndex);
		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", resultList);
		toJson.put("message", "查询首页发言列表成功");
		
		String result = JSONObject.fromObject(toJson).toString();
		System.out.println("+++++++" + result);
		return result;
	}

	
	/**
	 * @Description：发言详情
	 * @Author：刘浩浩
	 * @Date：2017年8月4日
	 * @param speakId 发言ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="speakDetail",method = {RequestMethod.POST,RequestMethod.GET})
	public String getSpeakDetail(String speakId) throws Exception{
		//结果封装
		Map<String, Object> toJson = new HashMap<String, Object>();
		if(StringUtils.isEmpty(speakId)){
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "发言ID为空");
			String result = JSONObject.fromObject(toJson).toString();
			return result;
		}
		SocialSpeakBean socialSpeakBean = socialSpeakService.getSocialSpeakBeanById(speakId);
		if(socialSpeakBean!=null){
			//人员属性
			toJson.put("userName", socialSpeakBean.getNickName());//发言人姓名 昵称
			toJson.put("headPicSrc", socialSpeakBean.getPhoto());//发言人头像
			
			//根据ID 获取话题集合
			List<SocialSubject> subjectList = socialSubjectService.findAllByfkId(speakId, SocialGlobal.SUB_RELATION_SPK);;
			Map<String, Object> subMap = new HashMap<String, Object>();
			if(subjectList!=null && subjectList.size()>0){
				for(SocialSubject sub : subjectList){
					subMap.put("id", sub.getId());
					subMap.put("subName", sub.getSubname());
				}
				
			}
			toJson.put("subjectList", subMap);
			
			//发言属性
			toJson.put("speakId", socialSpeakBean.getId());//发言id
			toJson.put("speakUserId", socialSpeakBean.getUserid());//发言人id
			String createTime = DateUtil.getDaysBeforeNow(socialSpeakBean.getCreatetime());
			toJson.put("createTime", createTime);//发言时间
			toJson.put("spContent", socialSpeakBean.getContent());//发言内容
			
			//获取发言图片集合
			toJson.put("releasePictures", socialSpeakBean.getImages());
			
			//发言相关属性
			toJson.put("isFocus", socialSpeakBean.getIsFocus());//是否对发言人关注
			toJson.put("isPraise", socialSpeakBean.getIsPraise());//是否点赞
			toJson.put("spCountFwd", socialSpeakBean.getCountForward());//转发数量
			toJson.put("spCountCmt", socialSpeakBean.getCountComment());//评论数量
			toJson.put("spCountPraise", socialSpeakBean.getCountPraise());//点赞数量
			
		}
		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("message", "查看发言详情成功");
		String result = JSONObject.fromObject(toJson).toString();
		System.out.println("+++++++" + result);
		return result;
	}
}