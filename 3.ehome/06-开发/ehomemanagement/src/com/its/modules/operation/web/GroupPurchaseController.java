/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.operation.web;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.its.common.config.Global;
import com.its.common.web.BaseController;
import com.its.common.utils.FileUtils;
import com.its.common.utils.MyFDFSClientUtils;
import com.its.common.utils.StringUtils;
import com.its.modules.module.entity.ModuleManage;
import com.its.modules.module.service.ModuleManageService;
import com.its.modules.operation.entity.GroupPurchase;
import com.its.modules.operation.entity.GroupPurchasetime;
import com.its.modules.operation.service.GroupPurchaseService;
import com.its.modules.operation.service.GroupPurchasetimeService;

/**
 * 团购管理Controller
 * @author caojing
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/operation/groupPurchase")
public class GroupPurchaseController extends BaseController {

	@Autowired
	private GroupPurchaseService groupPurchaseService;
	
	@Autowired
	private GroupPurchasetimeService groupPurchasetimeService; 
	
	@Autowired
	private ModuleManageService moduleManageService;
	
	@ModelAttribute
	public GroupPurchase get(@RequestParam(required=false) String id) {
		GroupPurchase entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = groupPurchaseService.get(id);
		}
		if (entity == null){
			entity = new GroupPurchase();
		}
		return entity;
	}
	
	@RequiresPermissions("operation:groupPurchase:view")
	@RequestMapping(value = {"list", ""})
	public String list(GroupPurchase groupPurchase, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		//列表页全部商家取值
		List<GroupPurchase> groupBusinessInfoList = new ArrayList<GroupPurchase>();
		GroupPurchase groupAll = new GroupPurchase();
		groupBusinessInfoList = groupPurchaseService.getAllBusinessList();
		groupAll.setBusinessinfoId("");
		groupAll.setBusinessName("全部商家");
		groupBusinessInfoList.add(0,groupAll);
		
		model.addAttribute("groupBusinessInfoList", groupBusinessInfoList);
		
		//页面上团购活动状态为不为空，查询按钮按下
		if(groupPurchase !=null && StringUtils.isNotBlank(groupPurchase.getGroupPurcState())){
			String groupPurcState = groupPurchase.getGroupPurcState();
			model.addAttribute("groupPurcState", groupPurcState);
		}else{
			//活动中
			groupPurchase.setGroupPurcState("1");
			groupPurchase.setBusinessinfoId("");
			model.addAttribute("groupPurcState", "1");
		}

		//列表页查询
		List<GroupPurchase> groupList = groupPurchaseService.findList(groupPurchase); 
		
		//设置图片路径
		for(GroupPurchase group : groupList){
			//根据图片ID取得图片SRC
			try {
				group.setGroupPurcPic(MyFDFSClientUtils.get_fdfs_file_url(request,group.getGroupPurcPic()));
			} catch (IOException | MyException e) {
				e.printStackTrace();
			}
		}
		
		//团购：进行中列表
		if("1".equals(groupPurchase.getGroupPurcState())){
			model.addAttribute("listSize", groupList.size());
		}
		
		model.addAttribute("groupList", groupList);
		
		return "modules/operation/groupPurchaseList";
	}

	@RequiresPermissions(value = { "operation:groupPurchase:add", "operation:groupPurchase:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(GroupPurchase groupPurchase, Model model,HttpServletRequest request) {
		
		//取模块下拉框的值（生活类模块）
		ModuleManage moduleManage = new ModuleManage();
		List<ModuleManage> lifeModuleList= moduleManageService.getLifeModuleList(moduleManage);
		moduleManage.setId("");
		moduleManage.setModuleName("模块名称");
		lifeModuleList.add(0, moduleManage);
		model.addAttribute("lifeModuleList", lifeModuleList);
		
		//新增的场合
		if(StringUtils.isBlank(groupPurchase.getId())){			
			//取商家下拉框的值
			GroupPurchase groupBusiness = new GroupPurchase();
			List<GroupPurchase> groupBusinessList = new ArrayList<GroupPurchase>();
			groupBusiness.setBusinessinfoId("");
			groupBusiness.setBusinessName("商家名称");
			groupBusinessList.add(0,groupBusiness);
			
			model.addAttribute("groupBusinessList", groupBusinessList);
			
		}else{
			//变更时，取商家下拉框的值
			ModuleManage moduleManageBusiness = new ModuleManage();
			List<GroupPurchase> groupBusinessList = new ArrayList<GroupPurchase>();
			//模块ID
			moduleManageBusiness.setId(groupPurchase.getModuleId());
			ModuleManage moduleBusinessCategory = moduleManageService.getBusinessCategoryDictId(moduleManageBusiness);
			if(moduleBusinessCategory !=null && moduleBusinessCategory.getBusinessCategoryDictId() !=null){
				//从商户分类信息表，依据商户分类ID，查询商户基本信息ID
				groupPurchase.setBusinessCategoryDictId(moduleBusinessCategory.getBusinessCategoryDictId());
				List<GroupPurchase> businessCategoryList = groupPurchaseService.getBusinessId(groupPurchase);
				if(businessCategoryList != null && businessCategoryList.size() > 0){
					for(GroupPurchase groupCategory : businessCategoryList){
						GroupPurchase groupBusiness =new GroupPurchase();
						//依据商家Id，从商家信息表查询商家名称
						groupBusiness.setBusinessinfoId(groupCategory.getBusinessinfoId());
						GroupPurchase groupBusinessInfo = groupPurchaseService.getBusinessNameList(groupBusiness);
						if(groupBusinessInfo !=null){							
						    groupBusinessList.add(groupBusinessInfo);
						}
					}
				}
			}
			
			GroupPurchase groupBusinessFirst =new GroupPurchase();
			groupBusinessFirst.setBusinessinfoId("");
			groupBusinessFirst.setBusinessName("商家名称");
			groupBusinessList.add(0,groupBusinessFirst);
		    
			model.addAttribute("groupBusinessList", groupBusinessList);
			
		}
		
		//根据图片ID取得图片SRC
		try {
			groupPurchase.setGroupPurcPic(MyFDFSClientUtils.get_fdfs_file_url(request,groupPurchase.getGroupPurcPic()));
		} catch (IOException | MyException e) {
			e.printStackTrace();
		}
		
		model.addAttribute("groupPurchase", groupPurchase);
		return "modules/operation/groupPurchaseForm";
	}

	/**
	 * 团购保存
	 * @param groupPurchase
	 * @param model
	 * @param file
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws ParseException
	 */
	@RequiresPermissions(value = { "operation:groupPurchase:add", "operation:groupPurchase:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(GroupPurchase groupPurchase, Model model,  @RequestParam(value = "file", required = false) MultipartFile file, 
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws ParseException{
		
		//实体类验证
		if (!beanValidator(model, groupPurchase)){
			return form(groupPurchase, model,request);
		}
		
		if(file.getSize() > 0){
		    //设置图片路径
		    groupPurchase.setGroupPurcPic(FileUtils.uploadImg(request,file));
		}
		//设置团购详情
        if (StringUtils.isNotBlank(groupPurchase.getGroupPurcDetail())) {
        	groupPurchase.setGroupPurcDetail(StringEscapeUtils.unescapeHtml4(groupPurchase.getGroupPurcDetail()));
        }
        
        //设置使用规则
        if (StringUtils.isNotBlank(groupPurchase.getUseRule())) {
        	groupPurchase.setUseRule(StringEscapeUtils.unescapeHtml4(groupPurchase.getUseRule()));
        }
        
		//保存团购管理信息
		int result = groupPurchaseService.saveGroupPurchase(groupPurchase);
		
		//主表保存成功
		if(result > 0){
			addMessage(redirectAttributes, "保存团购成功");
		}else{
			addMessage(redirectAttributes, "保存团购失败");
		}
				
		return "redirect:"+Global.getAdminPath()+"/operation/groupPurchase/?repage";
	}
	
	/**
	 * 团购删除
	 * @param groupPurchase
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("operation:groupPurchase:delete")
	@RequestMapping(value = "delete")
	public String delete(GroupPurchase groupPurchase, RedirectAttributes redirectAttributes) {
		groupPurchaseService.delete(groupPurchase);
		addMessage(redirectAttributes, "删除团购成功");
		return "redirect:"+Global.getAdminPath()+"/operation/groupPurchase/?repage";
	}
	
	/**
	 * 团购下线
	 * @param groupPurchase
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("operation:groupPurchase:offLine")
	@RequestMapping(value = "offLine")
	public String offLine(GroupPurchase groupPurchase, RedirectAttributes redirectAttributes) {
		int result = groupPurchaseService.updateState(groupPurchase);
		if(result > 0){
			addMessage(redirectAttributes, "团购下线成功");
		}else{
			addMessage(redirectAttributes, "团购下线失败");
		}
		
		return "redirect:"+Global.getAdminPath()+"/operation/groupPurchase/?repage";
	}
	
	/**
	 * 商家下拉框的取值
	 * @param moduleId 模块ID
	 * @return
	 */
	@RequestMapping(value = "getModuleList",method={RequestMethod.GET})
    @ResponseBody
    public List<GroupPurchase> getModuleList(@RequestParam(required=false) String moduleId) {

		List<GroupPurchase> groupBusinessInfoList = new ArrayList<GroupPurchase>();
		ModuleManage moduleManage = new ModuleManage();
		GroupPurchase groupPurchase = new GroupPurchase();
		GroupPurchase groupBusiness = new GroupPurchase();
		
		//模块ID
		moduleManage.setId(moduleId);
		ModuleManage moduleBusinessCategory = moduleManageService.getBusinessCategoryDictId(moduleManage);
		if(moduleBusinessCategory !=null && moduleBusinessCategory.getBusinessCategoryDictId() !=null){
			//从商户分类信息表，依据商户分类ID，查询商户基本信息ID
			groupPurchase.setBusinessCategoryDictId(moduleBusinessCategory.getBusinessCategoryDictId());
			List<GroupPurchase> businessCategoryList = groupPurchaseService.getBusinessId(groupPurchase);
			if(businessCategoryList != null && businessCategoryList.size() > 0){
				for(GroupPurchase groupCategory : businessCategoryList){
					//依据商家Id，从商家信息表查询商家名称
					groupBusiness.setBusinessinfoId(groupCategory.getBusinessinfoId());
					GroupPurchase groupBusinessInfo = groupPurchaseService.getBusinessNameList(groupBusiness);
					if(groupBusinessInfo !=null){
					  groupBusinessInfoList.add(groupBusinessInfo);
					}
				}
			}
		}
		groupPurchase.setBusinessinfoId("");
		groupPurchase.setBusinessName("商家名称");
		groupBusinessInfoList.add(0,groupPurchase);
		
        return groupBusinessInfoList;       
    }
	
	/**
	 * 编辑团购的场合，绑定团购时间信息用
	 * 
	 * @param groupPurchaseId 团购ID
	 * @return
	 * @throws ParseException 
	 */

	@ResponseBody
	@RequestMapping(value = "bindList")
	public List<GroupPurchasetime> bindList(String groupPurchaseId) throws ParseException {
		List<GroupPurchasetime> list = new ArrayList<GroupPurchasetime>();
		//设置条件
		GroupPurchasetime groupPurchasetime = new GroupPurchasetime();
		groupPurchasetime.setGroupPurchaseId(groupPurchaseId);
		list = groupPurchasetimeService.findList(groupPurchasetime);
		
		return list;
	}

	/**
	 * 保存顺序
	 * 
	 * @param groupPurchaseId 团购ID
	 * @return
	 */
	@RequiresPermissions("operation:groupPurchase:sort")
	@ResponseBody
	@RequestMapping(value = "saveNumSort")
	public String saveNumSort(String groupId, String sortNum) {
		
		String  result="1";
		
		//列表也存在排序数据
		if(StringUtils.isNotBlank(groupId)){
			
			GroupPurchase group = new GroupPurchase();
			//查询团购数据
			List<GroupPurchase> groupList = groupPurchaseService.findAllList(group);
			for(GroupPurchase groupAll:groupList){
				GroupPurchase groupPurchase = new GroupPurchase();
				groupPurchase.setId(groupAll.getId());
				groupPurchase.setSortNum("0");
				
				groupPurchaseService.updateSortNum(groupPurchase);
			}
			
			//团购主键
			String[] groupIds=groupId.split(",");
			//排序number
			String[] sortNums=sortNum.split(",");
			
			for(int i=1;i<groupIds.length;i++){
				GroupPurchase groupPurchase = new GroupPurchase();
				groupPurchase.setId(groupIds[i]);
				groupPurchase.setSortNum(sortNums[i]);
				
				int resultNum = groupPurchaseService.updateSortNum(groupPurchase);
				//更新不成功
				if(resultNum == 0){
					result = "0";
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 查询团购详情页数据
	 * @param groupPurchase
	 * @param model
	 * @return
	 */
	@RequiresPermissions("operation:groupPurchase:view")
	@RequestMapping(value = "detail")
	public String getDetail(GroupPurchase groupPurchase, Model model,HttpServletRequest request) {
		GroupPurchase groupPurchaseDetail = new GroupPurchase();
		//获取团购详情主表信息
		if(groupPurchase !=null && StringUtils.isNoneBlank(groupPurchase.getId())){
			groupPurchaseDetail = groupPurchaseService.getDetail(groupPurchase.getId());
			//商家支持设置
			if(groupPurchaseDetail !=null){				
				model.addAttribute("supportTypeList", groupPurchaseDetail.getSupportTypeList());
			}
			
			model.addAttribute("id", groupPurchase.getId());
		}
		
		// 根据图片ID取得图片SRC
		try {
			groupPurchaseDetail.setGroupPurcPic(MyFDFSClientUtils.get_fdfs_file_url(request,groupPurchaseDetail.getGroupPurcPic()));
		} catch (IOException | MyException e) {
			e.printStackTrace();
		}
		model.addAttribute("groupPurchase", groupPurchaseDetail);
		return "modules/operation/groupPurchaseDetail";
	}
}