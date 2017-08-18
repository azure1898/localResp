package com.its.modules.social.web;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.its.modules.app.entity.Account;
import com.its.modules.app.service.AccountService;
import com.its.modules.social.bean.SocialSpeakBean;
import com.its.modules.social.common.DateUtil;
import com.its.modules.social.common.SocialGlobal;
import com.its.modules.social.entity.SocialSubject;
import com.its.modules.social.service.SocialSpeakService;
import com.its.modules.social.service.SocialSubjectService;

import net.sf.json.JSONObject;

/**
 * @Description：话题相关接口
 * @Author：王萌萌
 * @Date：2017年8月10日
 */
@Controller
@RequestMapping(value = "${appPath}/subject")
public class SocialSubjectController extends BaseController {
	
	@Autowired
	private SocialSpeakService socialSpeakService;
	
	@Autowired
	private SocialSubjectService socialSubjectService;
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * @Description：话题结果
	 * @Author：王萌萌
	 * @Date：2017年8月10日
	 * @param addressId 楼盘ID
	 * @param subjectName 话题名称
	 * @param userId 当前用户id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="listSubject",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map listSubject(String addressId, String subjectName, String subjectId, String userId, int pageIndex) throws Exception {
		Map toJson = new HashMap();
		if(StringUtils.isEmpty(addressId)) {
			toJson.put("code", Global.CODE_ERROR);
			toJson.put("message", "楼盘id为空");
			return toJson;
		}
		if(StringUtils.isEmpty(userId)) {
			toJson.put("code", Global.CODE_ERROR);
			toJson.put("message", "当前用户id为空");
			return toJson;
		}
		List dataList = new ArrayList();
		//根据楼盘id和话题名称查询发言
		int pageSize = pageIndex == 0 ? SocialGlobal.PAGE_SIZE_INDEX : SocialGlobal.PAGE_SIZE;
		List<SocialSpeakBean> ssBeanList = socialSpeakService.findByAddIdAndSubName(addressId, subjectName, subjectId, userId, pageIndex, pageSize);
		if(!StringUtils.isEmpty(ssBeanList)) {
			for(SocialSpeakBean socialSpeakBean : ssBeanList) {
				Map dataListMap = new HashMap();
				dataListMap.put("userName", socialSpeakBean.getNickName());
				dataListMap.put("headPicSrc", socialSpeakBean.getPhoto());
				String createTime = DateUtil.getDaysBeforeNow(socialSpeakBean.getCreatetime());
				dataListMap.put("createTime", createTime);
				dataListMap.put("speakContent", socialSpeakBean.getContent());
				dataListMap.put("countForword", socialSpeakBean.getCountForward());
				dataListMap.put("countComment", socialSpeakBean.getCountComment());
				dataListMap.put("countPraise", socialSpeakBean.getCountPraise());
				dataListMap.put("isPraise", socialSpeakBean.getIsPraise());
				String image = socialSpeakBean.getImages();
				if(!StringUtils.isEmpty(image)) {
					String[] images = image.split(",");
					List imgList = Arrays.asList(images);
					dataListMap.put("imgList", imgList);
				} else {
					dataListMap.put("imgList", "");
				}
				List<SocialSubject> ssList = socialSubjectService.findAllByfkId(socialSpeakBean.getId(), SocialGlobal.SUB_RELATION_SPK);
				List subjectList = new ArrayList();
				for (SocialSubject ss : ssList) {
					Map subjectListMap = new HashMap();
					subjectListMap.put("id", ss.getId());
					subjectListMap.put("subName", ss.getSubname());
					subjectList.add(subjectListMap);
				}
				dataListMap.put("subjectList", subjectList);
				dataList.add(dataListMap);
			}
		}
		toJson.put("data", dataList);
		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("message", "根据楼盘id和话题名称查询发言成功");
		System.out.println("+++++++" + JSONObject.fromObject(toJson).toString());
		return toJson;
	}
	
	/**
	 * @Description：话题搜索
	 * @Author：王萌萌
	 * @Date：2017年8月10日
	 * @param subjectName 话题名称
	 * @param pageIndex 
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="subSearch",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map subSearch(String subjectName, int pageIndex) throws Exception {
		Map toJson = new HashMap();
		if(StringUtils.isEmpty(subjectName)) {
			toJson.put("code", Global.CODE_ERROR);
			toJson.put("message", "话题名称为空");
			return toJson;
		}
		List dataList = new ArrayList<>();
		int countSub = socialSubjectService.findSocialSubjectBySubName(subjectName);
		if(countSub <= 0) {
			Map dataListMap = new HashMap();
			dataListMap.put("id", "");
			dataListMap.put("subName", subjectName);
			dataList.add(dataListMap);
		}
		
		int pageSize = pageIndex == 0 ? SocialGlobal.PAGE_SIZE_INDEX : SocialGlobal.PAGE_SIZE;
		List<SocialSubject> ssList = socialSubjectService.findBySubName(subjectName, pageIndex, pageSize);
		for(SocialSubject socialSubject : ssList) {
			Map dataListMap = new HashMap();
			dataListMap.put("id", socialSubject.getId());
			dataListMap.put("subName", socialSubject.getSubname());
			dataList.add(dataListMap);
		}
		toJson.put("data", dataList);
		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("message", "话题搜索成功");
		return toJson;
	}
	
	/**
	 * @Description：保存话题
	 * @Author：王萌萌
	 * @Date：2017年8月14日
	 * @param subName 话题名称
	 * @param villageInfoId 楼盘id
	 * @param createrId 当前用户id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="saveSubject",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map saveSubject(String subName, String villageInfoId, String createrId) throws Exception {
		Map toJson = new HashMap();
		if(StringUtils.isEmpty(subName)) {
			toJson.put("code", Global.CODE_ERROR);
			toJson.put("message", "话题名称为空");
			return toJson;
		}
		if(StringUtils.isEmpty(villageInfoId)) {
			toJson.put("code", Global.CODE_ERROR);
			toJson.put("message", "楼盘id为空");
			return toJson;
		}
		if(StringUtils.isEmpty(createrId)) {
			toJson.put("code", Global.CODE_ERROR);
			toJson.put("message", "当前用户id为空");
			return toJson;
		}
		//查看当前输入的话题是否已经存在 //如果存在，不用保存 //如果不存在，保存
		int countSub = socialSubjectService.findSocialSubjectBySubName(subName);
		if(countSub == 0) {
			SocialSubject socialSubject = new SocialSubject();
			socialSubject.setSubname(subName);
			socialSubject.setVillageInfoId(villageInfoId);
			socialSubject.setCreaterid(createrId);
			socialSubject.setCreatetime(new Date());
			socialSubject.setIsrecommend(SocialGlobal.SUBJECT_IS_RECOMMEND_NO);
			Account account = accountService.get(createrId);
			socialSubject.setCreatername(account.getNickname());
			
			socialSubjectService.save(socialSubject);
			toJson.put("id", socialSubject.getId());
		}
		
		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("message", "保存话题成功");
		
		return toJson;
	}
	
	/**
	 * @Description：查询出所有热门话题
	 * @Author：王萌萌
	 * @Date：2017年8月15日
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="hotSubjectList",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map hotSubjectList() throws Exception {
		Map toJson = new HashMap();
		List<SocialSubject> socialSubjectList = socialSubjectService.findHotSubjectList(SocialGlobal.SUBJECT_IS_RECOMMEND_YES);
		List dataList = new ArrayList();
		if(socialSubjectList.size()>0 && !StringUtils.isEmpty(socialSubjectList)) {
			for(SocialSubject socialSubject : socialSubjectList) {
				Map dataListMap = new HashMap();
				dataListMap.put("id", socialSubject.getId());
				dataListMap.put("subtName", socialSubject.getSubname());
				dataList.add(dataListMap);
			}
		}
		toJson.put("data", dataList);
		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("message", "查询所有热门话题成功");
		return toJson;
	}

}
