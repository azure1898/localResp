/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.sys.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.its.common.config.Global;
import com.its.common.utils.StringUtils;
import com.its.common.web.BaseController;
import com.its.modules.sys.entity.Area;
import com.its.modules.sys.service.AreaService;

/**
 * 区域Controller
 * @author Jetty
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;
	
	@ModelAttribute("area")
	public Area get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return areaService.get(id);
		}else{
			return new Area();
		}
	}

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = {"list", ""})
	public String list(Area area, Model model) {
		List<Area> list = areaService.getAreaList(area);
		model.addAttribute("list", list);
		model.addAttribute("proId", area.getAddrPro());
        model.addAttribute("cityId", area.getAddrCity());
        model.addAttribute("areaId", area.getAddrArea());
		return "modules/sys/areaList";
	}

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "form")
	public String form(Area area, Model model,String mode) {
		//添加下一级区域的场合，上级区域不可选择
		if("subAdd".equals(mode)){
			model.addAttribute("editFlag","1");
		}
				
		//if (area.getParent()==null||area.getParent().getId()==null){
		//	area.setParent(UserUtils.getUser().getOffice().getArea());
		//}
		
		if(area.getParent()!=null && area.getParent().getId()!=null){
		    area.setParent(areaService.get(area.getParent().getId()));
		}

		model.addAttribute("area", area);
		return "modules/sys/areaForm";
	}
	
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "save")
	public String save(Area area, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/area";
		}
		if (!beanValidator(model, area)){
			return form(area, model,"");
		}
		areaService.save(area);
		addMessage(redirectAttributes, "保存区域'" + area.getName() + "'成功");
		return "redirect:" + adminPath + "/sys/area/";
	}
	
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "delete")
	public String delete(Area area, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/area";
		}
//		if (Area.isRoot(id)){
//			addMessage(redirectAttributes, "删除区域失败, 不允许删除顶级区域或编号为空");
//		}else{
			areaService.delete(area);
			addMessage(redirectAttributes, "删除区域成功");
//		}
		return "redirect:" + adminPath + "/sys/area/";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Area area = new Area();
		List<Area> list = areaService.getAreaList(area);
		for (int i=0; i<list.size(); i++){
			Area e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 获取全部省份(第一级别的列表)
	 * @author zhujiao
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="loadPro", method={RequestMethod.POST,RequestMethod.GET})
	public List<Area> loadPro(HttpServletRequest request){
		List<Area> allList = areaService.findAll();
		List<Area> proList = new ArrayList<Area>();
		for(Area area : allList){
			if("2".equals(area.getType())){
				proList.add(area);
			}
		}
		return proList;
	}
	
	/**
	 * 获取全部城市（第二级别列表）
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="loadCity", method={RequestMethod.POST,RequestMethod.GET})
	public List<Area> loadCity(HttpServletRequest request){
		List<Area> allList = areaService.findAll();
		List<Area> cityList = new ArrayList<Area>();
		for(Area area : allList){
			if("3".equals(area.getType())){
				cityList.add(area);
			}
		}
		return cityList;
	}
	/**
	 * 获取全部区域数据 (第三级别的列表)
	 * @param request
	 * @param parentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="changeArea", method={RequestMethod.POST,RequestMethod.GET})
	public List<Area> changeArea(HttpServletRequest request,@RequestParam String parentId){
		List<Area> allList = areaService.findAll();
		List<Area> cityList = new ArrayList<Area>();
		for(Area area : allList){
			if(parentId.equals(area.getParentId())){
				cityList.add(area);
			}
		}
		return cityList;
	}
}
